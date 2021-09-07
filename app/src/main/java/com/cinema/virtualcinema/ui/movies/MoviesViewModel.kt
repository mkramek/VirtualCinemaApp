package com.cinema.virtualcinema.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinema.virtualcinema.data.model.Movie
import com.cinema.virtualcinema.data.repository.MoviesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel constructor(private val repository: MoviesRepository): ViewModel() {
    val movieList = MutableLiveData<List<Movie>>()
    val error = MutableLiveData<String>()

    fun getMovies() {
        val response = repository.getMovies()
        response.enqueue(object: Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                movieList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }
}