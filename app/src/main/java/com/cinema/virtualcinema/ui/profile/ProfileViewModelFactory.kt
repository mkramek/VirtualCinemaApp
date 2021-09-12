package com.cinema.virtualcinema.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinema.virtualcinema.data.repository.ReservationRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.cinema.virtualcinema.ui.movies.MoviesViewModel
import java.lang.IllegalArgumentException

class ProfileViewModelFactory constructor(private val repository: ReservationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}