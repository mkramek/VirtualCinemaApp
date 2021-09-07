package com.cinema.virtualcinema.data.repository

import com.cinema.virtualcinema.data.service.SeatsService

class SeatsRepository constructor(private val seatsService: SeatsService) {
    fun getSeats() = seatsService.getSeats()
    fun getSeatsByRoom(roomId: Long) = seatsService.getSeatsByRoom(roomId)
}