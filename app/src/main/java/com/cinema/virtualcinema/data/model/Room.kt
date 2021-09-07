package com.cinema.virtualcinema.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(var id: Long, var roomName: String): Parcelable