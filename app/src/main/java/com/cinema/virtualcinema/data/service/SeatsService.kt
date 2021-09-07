package com.cinema.virtualcinema.data.service

import com.cinema.virtualcinema.data.model.Room
import com.cinema.virtualcinema.data.model.Seat
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface SeatsService {
    @GET("seat")
    fun getSeats(): Call<List<Seat>>

    @GET("seat/room/{id}")
    fun getSeatsByRoom(@Path("id") roomId: Long): Call<List<Seat>>

    companion object {
        var service: SeatsService? = null

        fun getInstance(): SeatsService {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.22:8080/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                service = retrofit.create(SeatsService::class.java)
            }
            return service!!
        }
    }
}