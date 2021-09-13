package com.cinema.virtualcinema.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.data.model.Reservation
import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.repository.ReservationRepository
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.cinema.virtualcinema.data.service.ReservationService
import com.cinema.virtualcinema.data.service.SeatStatusService
import com.cinema.virtualcinema.data.service.SeatsService
import com.cinema.virtualcinema.databinding.FragmentProfileBinding
import com.cinema.virtualcinema.ui.OnDeleteRequestListener
import com.cinema.virtualcinema.ui.reservationseats.ReservationSeatsAdapter
import com.cinema.virtualcinema.ui.reservationseats.ReservationSeatsViewModel
import com.cinema.virtualcinema.ui.reservationseats.ReservationSeatsViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private val serviceHost = "http://192.168.1.22:8080/api/v1/"
    private val reservationRepository = ReservationRepository(ReservationService.getInstance(serviceHost))
    private val reservationsAdapter = ReservationsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = view.context.getSharedPreferences("local_user.preferences", Context.MODE_PRIVATE)
        val uuid = prefs.getString("user@uuid", "")
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(reservationRepository)).get(
            ProfileViewModel::class.java)
        reservationsAdapter.setOnDeleteRequestListener(object: OnDeleteRequestListener {
            override fun onDeleteRequest(itemId: Long) {
                val deleteCall = reservationRepository.deleteReservation(itemId)
                deleteCall.enqueue(object: Callback<String> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        viewModel.deleteReservation(itemId)
                        reservationsAdapter.onDeleteRequestDone()
                        Toast.makeText(view.context, "Usunięto rezerwację", Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        viewModel.deleteReservation(itemId)
                        reservationsAdapter.onDeleteRequestDone()
                        Log.e("RSRV/DELETE", t.message!!)
                    }
                })
            }
        })
        val recyclerView: RecyclerView = view.findViewById(R.id.profile_recyclerview)
        recyclerView.apply {
            adapter = reservationsAdapter
            layoutManager = LinearLayoutManager(this.context)
            this.setHasFixedSize(true)
        }

        viewModel.reservationList.observe(viewLifecycleOwner, Observer {
            Log.d("SEAT_API", "onCreate: $it")
            reservationsAdapter.setReservations(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Log.d("SEAT_API", "onError: $it")
        })

        viewModel.getReservationsByUser(uuid!!)
    }
}