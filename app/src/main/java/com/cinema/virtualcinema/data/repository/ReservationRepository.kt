package com.cinema.virtualcinema.data.repository

import com.cinema.virtualcinema.data.model.Reservation
import com.cinema.virtualcinema.data.service.ReservationService

class ReservationRepository constructor(private val reservationService: ReservationService) {
    fun getReservations() = reservationService.getReservations()
    fun getReservationsByUser(user: String) = reservationService.getReservationsByUser(user)
    fun getReservationsByRoom(roomId: Long) = reservationService.getReservationsByRoom(roomId)
    fun getReservationsBySeat(seatId: Long) = reservationService.getReservationsBySeat(seatId)
    fun createReservation(reservation: Reservation) = reservationService.createReservation(reservation)
    fun deleteReservation(reservationID: Long) = reservationService.deleteReservation(reservationID)
}