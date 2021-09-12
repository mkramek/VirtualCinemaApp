package com.cinema.virtualcinema.data.model

data class Seat(val id: Long, val seatRow:Char, val seatNumber:Number, val room: Room) {
    override fun toString(): String {
        return "${seatRow}${seatNumber}"
    }
}