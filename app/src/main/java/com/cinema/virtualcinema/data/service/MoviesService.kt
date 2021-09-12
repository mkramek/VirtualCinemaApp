package com.cinema.virtualcinema.data.service

import com.cinema.virtualcinema.data.model.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MoviesService {
    @GET("movie")
    fun getMovies(): Call<List<Movie>>

    companion object {
        var service: MoviesService? = null

        fun getInstance(url: String): MoviesService {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                service = retrofit.create(MoviesService::class.java)
            }
            return service!!
        }
    }
}