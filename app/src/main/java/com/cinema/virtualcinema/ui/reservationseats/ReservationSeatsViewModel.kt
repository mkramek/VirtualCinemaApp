package com.cinema.virtualcinema.ui.reservationseats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinema.virtualcinema.data.model.Room
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.repository.SeatsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationSeatsViewModel constructor(private val repository: SeatsRepository) : ViewModel() {
    val seatList = MutableLiveData<List<Seat>>()
    val error = MutableLiveData<String>()

    fun getSeats() {
        val response = repository.getSeats()
        response.enqueue(object: Callback<List<Seat>> {
            override fun onResponse(call: Call<List<Seat>>, response: Response<List<Seat>>) {
                seatList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Seat>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }

    fun getSeatsByRoom(roomId: Long) {
        val response = repository.getSeatsByRoom(roomId)
        response.enqueue(object: Callback<List<Seat>> {
            override fun onResponse(call: Call<List<Seat>>, response: Response<List<Seat>>) {
                seatList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Seat>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }
}