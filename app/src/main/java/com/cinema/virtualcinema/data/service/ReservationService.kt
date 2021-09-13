package com.cinema.virtualcinema.data.service

import com.cinema.virtualcinema.data.model.Reservation
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReservationService {
    @GET("reservation")
    fun getReservations(): Call<List<Reservation>>

    @GET("reservation/room/{id}")
    fun getReservationsByRoom(@Path("id") roomId: Long): Call<List<Reservation>>

    @GET("reservation/seat/{id}")
    fun getReservationsBySeat(@Path("id") seatId: Long): Call<Reservation>

    @GET("reservation/user/{id}")
    fun getReservationsByUser(@Path("id") user: String): Call<List<Reservation>>

    @POST("reservation")
    fun createReservation(@Body reservation: Reservation): Call<Reservation>

    @DELETE("reservation/{id}")
    fun deleteReservation(@Path("id") reservationID: Long): Call<String>

    companion object {
        var service: ReservationService? = null

        fun getInstance(url: String): ReservationService {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                service = retrofit.create(ReservationService::class.java)
            }
            return service!!
        }
    }
}