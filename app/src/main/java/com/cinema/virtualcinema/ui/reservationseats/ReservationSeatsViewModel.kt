package com.cinema.virtualcinema.ui.reservationseats

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ReservationSeatsViewModel constructor(private val seatsRepository: SeatsRepository, private val statusRepository: SeatStatusRepository) : ViewModel() {
    val seatList = MutableLiveData<List<Seat>>()
    val statusList = MutableLiveData<ArrayList<SeatStatus>>()
    val error = MutableLiveData<String>()

    private val pollingChannel = Channel<Deferred<List<SeatStatus>>>()
    private val POLLING_TIMEOUT = 10000L
    private val POLLING_FREQUENCY = 2000L

    private var sync = false

    fun getSeatsByRoom(roomId: Long) {
        val response = seatsRepository.getSeatsByRoom(roomId)
        response.enqueue(object: Callback<List<Seat>> {
            override fun onResponse(call: Call<List<Seat>>, response: Response<List<Seat>>) {
                seatList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Seat>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }

    fun appendStatus(status: SeatStatus) {
        var statusFound = false
        val newStatuses = ArrayList(statusList.value!!)
        newStatuses.forEach {
            if (it.seat.id == status.seat.id) {
                statusFound = true
                it.status = status.status
                it.sender = status.sender
                return@forEach
            }
        }
        if (!statusFound) {
            newStatuses.add(status)
        }
        statusList.postValue(newStatuses);
    }

    fun updateStatus(status: SeatStatus) {
        val call: Call<SeatStatus> = statusRepository.updateStatus(status)
        call.enqueue(object: Callback<SeatStatus> {
            override fun onResponse(call: Call<SeatStatus>, response: Response<SeatStatus>) {
                val gson = Gson()
                if (response.body() == null) {
                    Log.e("API/SEATS", "Response is empty")
                    Log.d("API/REQUEST", gson.toJson(call.request()))
                } else {
                    appendStatus(response.body()!!)
                }
            }
            override fun onFailure(call: Call<SeatStatus>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun getStatusList(): List<SeatStatus> {
        val call = statusRepository.getStatuses()
        val statusRequest = call.execute()
        return if (statusRequest.isSuccessful) {
            if (statusRequest.body() == null) emptyList()
            else statusRequest.body()!!
        } else {
            emptyList()
        }
    }

    fun poll() {
        CoroutineScope(Dispatchers.Main).launch {
            for (task in pollingChannel) {
                val pollStatusList = task.await()
                if (pollStatusList.isNotEmpty()) setStatuses(pollStatusList)
                else Log.d("API/STATUS", "Empty response")
            }
        }

        GlobalScope.launch {
            withTimeoutOrNull(POLLING_TIMEOUT) {
                while (sync) {
                    pollingChannel.send(async {
                        getStatusList()
                    })
                    delay(POLLING_FREQUENCY)
                }
            }
        }
    }

    fun setStatuses(statuses: List<SeatStatus>) {
        this.statusList.value = statuses as ArrayList<SeatStatus>
    }

    fun getStatuses() {
        val response = statusRepository.getStatuses()
        response.enqueue(object: Callback<List<SeatStatus>> {
            override fun onResponse(call: Call<List<SeatStatus>>, response: Response<List<SeatStatus>>) {
                statusList.postValue(response.body() as ArrayList<SeatStatus>)
            }

            override fun onFailure(call: Call<List<SeatStatus>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }
}