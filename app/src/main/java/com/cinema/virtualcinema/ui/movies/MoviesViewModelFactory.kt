package com.cinema.virtualcinema.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinema.virtualcinema.data.repository.MoviesRepository
import java.lang.IllegalArgumentException

class MoviesViewModelFactory constructor(private val repository: MoviesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            MoviesViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}