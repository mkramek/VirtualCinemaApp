package com.cinema.virtualcinema.ui.reservationseats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import java.lang.IllegalArgumentException

class ReservationSeatsViewModelFactory constructor(private val seatsRepository: SeatsRepository, private val statusRepository: SeatStatusRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReservationSeatsViewModel::class.java)) {
            ReservationSeatsViewModel(this.seatsRepository, this.statusRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}