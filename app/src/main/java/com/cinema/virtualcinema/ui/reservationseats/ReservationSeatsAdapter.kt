package com.cinema.virtualcinema.ui.reservationseats

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.ReservationActivity
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.service.SeatStatusService
import com.cinema.virtualcinema.http.APIClient
import com.cinema.virtualcinema.http.APIInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ReservationSeatsAdapter : RecyclerView.Adapter<ReservationSeatsAdapter.SeatsViewHolder>() {

    private val serviceHost = "http://192.168.1.22:8080/api/v1"
    private var seats = mutableListOf<Seat>()
    private var statuses = mutableListOf<SeatStatus>()
    private val statusRepository = SeatStatusRepository(SeatStatusService.getInstance(serviceHost))

    @SuppressLint("NotifyDataSetChanged")
    fun setSeatList(seats: List<Seat>) {
        this.seats = seats.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStatuses(statuses: List<SeatStatus>) {
        statuses.forEach {
            appendStatus(it)
            Log.d("DATASET/STATUS", "status: ${it.status}, seat: ${it.seat}, dataset_size: ${statuses.size}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendStatus(status: SeatStatus) {
        var statusFound = false
        statuses.forEach {
            if (it.seat.id == status.seat.id) {
                statusFound = true
                it.status = status.status
                it.sender = status.sender
                notifyDataSetChanged()
                return@forEach
            }
        }
        if (!statusFound) {
            statuses.add(status)
            notifyDataSetChanged()
        } else return
    }

    override fun getItemCount() = seats.size

    private fun getSeatCountPerRow(row: Char): Int {
        var counter = 0
        seats.forEach {
            if (it.seatRow == row) {
                counter++
            }
        }
        return counter
    }

    private fun getRows(): List<Char> {
        var currRow = '*'
        val rowList = ArrayList<Char>()
        seats.forEach {
            if (it.seatRow != currRow) {
                currRow = it.seatRow
                rowList.add(it.seatRow)
            }
        }
        return rowList
    }

    fun getLongestRowLength(): Int {
        var count = 0
        getRows().forEach {
            val currCount = getSeatCountPerRow(it)
            if (currCount > count) {
                count = currCount
            }
        }
        return count
    }

    fun getReservedSeats(uuid: String): List<Seat> {
        val reservedSeats = ArrayList<Seat>()
        statuses.forEach {
            if (it.status == 2 && it.sender == uuid) reservedSeats.add(it.seat)
        }
        return reservedSeats
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SeatsViewHolder(inflater.inflate(R.layout.item_seat, parent, false))
    }

    override fun onBindViewHolder(holder: SeatsViewHolder, position: Int) = holder.bind(seats[position])

    inner class SeatsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Seat) {
            val prefs = view.context.getSharedPreferences("local_user.preferences", Context.MODE_PRIVATE)
            val uuid = prefs.getString("user@uuid", "")
            var currentSeatStatus: SeatStatus? = null
            statuses.forEach {
                if (it.seat.id == item.id) {
                    currentSeatStatus = it
                    view.findViewById<CardView>(R.id.seat_card)
                        .setCardBackgroundColor(
                            when (it.status) {
                                0 -> ContextCompat.getColor(view.context, R.color.seat_free)
                                1 -> ContextCompat.getColor(view.context, R.color.seat_taken)
                                else -> if (it.sender == uuid) ContextCompat.getColor(view.context, R.color.seat_reserved_by_me) else ContextCompat.getColor(view.context, R.color.seat_reserved)
                            }
                        )
                }
            }
            if (currentSeatStatus == null) {
                currentSeatStatus = SeatStatus(item, 0, uuid!!)
            }
            view.findViewById<TextView>(R.id.seat_id).text = item.toString()
            view.findViewById<CardView>(R.id.seat_card).setOnClickListener {
                if (currentSeatStatus!!.status == 0) {
                    currentSeatStatus!!.status = 2
                    currentSeatStatus!!.sender = uuid!!
                    updateStatus(currentSeatStatus!!)
                } else if (currentSeatStatus!!.status == 2 && currentSeatStatus!!.sender == uuid!!) {
                    currentSeatStatus!!.status = 0
                    updateStatus(currentSeatStatus!!)
                } else Toast.makeText(view.context, "Nie można wybrać miejsca", Toast.LENGTH_SHORT).show()
            }
        }

        private fun updateStatus(seatStatus: SeatStatus) {
            val call: Call<SeatStatus> = statusRepository.updateStatus(seatStatus)
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
    }
}