package com.cinema.virtualcinema.ui.profile

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
import com.cinema.virtualcinema.data.model.Reservation
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.repository.ReservationRepository
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.service.ReservationService
import com.cinema.virtualcinema.data.service.SeatStatusService
import com.cinema.virtualcinema.http.APIClient
import com.cinema.virtualcinema.http.APIInterface
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ReservationsAdapter : RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder>() {

    private var reservations = mutableListOf<Reservation>()

    @SuppressLint("NotifyDataSetChanged")
    fun setReservations(seats: List<Reservation>) {
        this.reservations = seats.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = reservations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReservationsViewHolder(inflater.inflate(R.layout.item_reservation, parent, false))
    }

    override fun onBindViewHolder(holder: ReservationsViewHolder, position: Int) = holder.bind(reservations[position])

    inner class ReservationsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Reservation) {
            view.findViewById<TextView>(R.id.reservation_id).text = item.id.toString()
            view.findViewById<TextView>(R.id.reservation_seat).text = item.seat.toString()
            view.findViewById<TextView>(R.id.reservation_room).text = item.seat.room.toString()
        }
    }
}