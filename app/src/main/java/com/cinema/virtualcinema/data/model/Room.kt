package com.cinema.virtualcinema.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(val id: Long, val roomName: String): Parcelable {
    override fun toString(): String {
        return "$roomName ($id)"
    }
}