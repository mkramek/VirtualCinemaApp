package com.cinema.virtualcinema.ui.reservationseats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.data.model.Seat
import java.lang.StringBuilder

class ReservationSeatsAdapter : RecyclerView.Adapter<ReservationSeatsAdapter.SeatsViewHolder>() {

    private var seats = mutableListOf<Seat>()

    @SuppressLint("NotifyDataSetChanged")
    fun setSeatList(seats: List<Seat>) {
        this.seats = seats.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = seats.size

    fun getSeatCountPerRow(row: Char): Int {
        var counter = 0
        seats.forEach {
            if (it.seatRow == row) {
                counter++
            }
        }
        return counter
    }

    fun getRows(): List<Char> {
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

    fun getRowCount(): Int {
        var rowCount = 0
        var prevChar = '*'
        seats.forEach {
            if (it.seatRow != prevChar) {
                prevChar = it.seatRow
                rowCount++
            }
        }
        return rowCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SeatsViewHolder(inflater.inflate(R.layout.item_seat, parent, false))
    }

    override fun onBindViewHolder(holder: SeatsViewHolder, position: Int) = holder.bind(seats[position])

    inner class SeatsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        fun bind(item: Seat) {
            val builder = StringBuilder()
            view.findViewById<TextView>(R.id.seat_id).text = builder
                .append(item.seatRow)
                .append(item.seatNumber)
                .toString()
        }
    }
}