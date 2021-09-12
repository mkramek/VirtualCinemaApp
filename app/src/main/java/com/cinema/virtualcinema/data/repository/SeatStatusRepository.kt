package com.cinema.virtualcinema.data.repository

import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.service.SeatStatusService

class SeatStatusRepository constructor(private val statusService: SeatStatusService) {
    fun getStatuses() = statusService.getAllStatuses()
    fun updateStatus(status: SeatStatus) = statusService.updateStatus(status)
}