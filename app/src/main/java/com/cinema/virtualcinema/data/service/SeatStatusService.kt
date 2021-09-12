package com.cinema.virtualcinema.data.service

import com.cinema.virtualcinema.data.model.Room
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.model.SeatStatus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SeatStatusService {
    @POST("status/update")
    fun updateStatus(@Body seat: SeatStatus): Call<SeatStatus>

    @GET("status")
    fun getAllStatuses(): Call<List<SeatStatus>>

    companion object {
        var service: SeatStatusService? = null

        fun getInstance(url: String): SeatStatusService {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                service = retrofit.create(SeatStatusService::class.java)
            }
            return service!!
        }
    }
}