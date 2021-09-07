package com.cinema.virtualcinema.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(var movieTitle:String, var room:Room) : Parcelable