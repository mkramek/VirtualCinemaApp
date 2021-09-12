package com.cinema.virtualcinema.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cinema.virtualcinema.data.model.Reservation
import com.cinema.virtualcinema.data.model.Room
import com.cinema.virtualcinema.data.model.Seat
import com.cinema.virtualcinema.data.repository.ReservationRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel constructor(private val repository: ReservationRepository) : ViewModel() {
    val reservationList = MutableLiveData<List<Reservation>>()
    val error = MutableLiveData<String>()

    fun getReservationsByUser(user: String) {
        val response = repository.getReservationsByUser(user)
        response.enqueue(object: Callback<List<Reservation>> {
            override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                reservationList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                error.postValue(t.message)
            }
        })
    }
}