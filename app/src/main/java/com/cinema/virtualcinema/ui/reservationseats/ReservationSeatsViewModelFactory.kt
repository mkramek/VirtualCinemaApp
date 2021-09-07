package com.cinema.virtualcinema.ui.reservationseats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.cinema.virtualcinema.ui.movies.MoviesViewModel
import java.lang.IllegalArgumentException

class ReservationSeatsViewModelFactory constructor(private val repository: SeatsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ReservationSeatsViewModel::class.java)) {
            ReservationSeatsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}