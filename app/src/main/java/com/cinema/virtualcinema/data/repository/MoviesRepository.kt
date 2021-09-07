package com.cinema.virtualcinema.data.repository

import com.cinema.virtualcinema.data.service.MoviesService

class MoviesRepository constructor(private val moviesService: MoviesService) {
    fun getMovies() = moviesService.getMovies()
}