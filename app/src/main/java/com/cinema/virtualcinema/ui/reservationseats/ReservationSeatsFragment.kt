package com.cinema.virtualcinema.ui.reservationseats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.data.model.Movie
import com.cinema.virtualcinema.data.model.Reservation
import com.cinema.virtualcinema.data.model.SeatStatus
import com.cinema.virtualcinema.data.repository.ReservationRepository
import com.cinema.virtualcinema.data.repository.SeatStatusRepository
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.cinema.virtualcinema.data.service.ReservationService
import com.cinema.virtualcinema.data.service.SeatStatusService
import com.cinema.virtualcinema.data.service.SeatsService
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationSeatsFragment : Fragment() {

    private lateinit var viewModel: ReservationSeatsViewModel

    private val serviceHost = "http://192.168.1.22:8080/api/v1/"
    private val seatsRepository = SeatsRepository(SeatsService.getInstance(serviceHost))
    private val reservationRepository = ReservationRepository(ReservationService.getInstance(serviceHost))
    private val statusRepository = SeatStatusRepository(SeatStatusService.getInstance(serviceHost))
    private val seatsAdapter = ReservationSeatsAdapter()

    companion object {
        fun newInstance() = ReservationSeatsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_reservation_seats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = view.context.getSharedPreferences("local_user.preferences", Context.MODE_PRIVATE)
        val uuid = prefs.getString("user@uuid", "")
        viewModel = ViewModelProvider(this, ReservationSeatsViewModelFactory(seatsRepository, statusRepository)).get(ReservationSeatsViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.seats_recyclerview)
        recyclerView.apply {
            adapter = seatsAdapter
            layoutManager = GridLayoutManager(this.context, 3)
            this.setHasFixedSize(true)
        }

        val call = statusRepository.getStatuses()

        call.enqueue(object: Callback<List<SeatStatus>> {
            override fun onResponse(call: Call<List<SeatStatus>>, response: Response<List<SeatStatus>>) {
                if (response.body() == null) {
                    val gson = Gson()
                    Log.e("API/STATUS", "Empty response")
                    Log.d("API/REQUEST", gson.toJson(call.request()))
                } else {
                    viewModel.setStatuses(response.body()!!)
                }
            }
            override fun onFailure(call: Call<List<SeatStatus>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        viewModel.seatList.observe(viewLifecycleOwner, {
            Log.d("SEAT_API", "onCreate: $it")
            seatsAdapter.setSeatList(it)
            recyclerView.layoutManager = GridLayoutManager(this.context, seatsAdapter.getLongestRowLength())
        })
        viewModel.statusList.observe(viewLifecycleOwner, {
            Log.d("STATUS/CREATE", "onCreate: $it")
            seatsAdapter.setStatuses(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Log.d("SEAT_API", "onError: $it")
        })

        arguments?.getParcelable<Movie>("movie")?.let {
            viewModel.getSeatsByRoom(it.room.id)
        }

        view.findViewById<Button>(R.id.confirm_seats_btn).setOnClickListener {
            val list = seatsAdapter.getReservedSeats(uuid!!)
            Log.d("RSRV/LIST", "items: ${list.size}")
            list.forEach {
                val reservation = Reservation(id = -1, seat = it, user = uuid)
                val makeCall = reservationRepository.createReservation(reservation)
                makeCall.enqueue(object: Callback<Reservation> {
                    override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                        if (response.body() == null) {
                            val gson = Gson()
                            Log.e("API/STATUS", "Empty response")
                            Log.d("API/REQUEST", gson.toJson(call.request()))
                        } else {
                            Log.d("RSRV/NEW", "seat: $it")
                        }
                    }
                    override fun onFailure(call: Call<Reservation>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
                val status = SeatStatus(seat = it, sender = uuid, status = 1)
                val updateCall = statusRepository.updateStatus(status)
                updateCall.enqueue(object: Callback<SeatStatus> {
                    override fun onResponse(call: Call<SeatStatus>, response: Response<SeatStatus>) {
                        if (response.body() == null) {
                            val gson = Gson()
                            Log.e("API/STATUS", "Empty response")
                            Log.d("API/REQUEST", gson.toJson(call.request()))
                        } else {
                            Log.d("STATUS/NEW", "seat: $it")
                        }
                    }
                    override fun onFailure(call: Call<SeatStatus>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }

        view.findViewById<Button>(R.id.cancel_seats_btn).setOnClickListener {
            seatsAdapter.getReservedSeats(uuid!!).forEach {
                val cancelledStatus = SeatStatus(seat = it, status = 0, sender = uuid)
                statusRepository.updateStatus(cancelledStatus)
            }
        }
    }
}