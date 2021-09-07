package com.cinema.virtualcinema.ui.reservationseats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.data.model.Movie
import com.cinema.virtualcinema.data.repository.SeatsRepository
import com.cinema.virtualcinema.data.service.SeatsService

class ReservationSeatsFragment : Fragment() {

    private lateinit var viewModel: ReservationSeatsViewModel

    private val seatsService = SeatsService.getInstance()
    private val seatsAdapter = ReservationSeatsAdapter()

    companion object {
        fun newInstance() = ReservationSeatsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.reservation_seats_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Movie>("movie").let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ReservationSeatsViewModelFactory(SeatsRepository(seatsService))).get(ReservationSeatsViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.seats_recyclerview)
        recyclerView.apply {
            adapter = seatsAdapter
            layoutManager = GridLayoutManager(this.context, 3)
            this.setHasFixedSize(true)
        }

        viewModel.seatList.observe(viewLifecycleOwner, Observer {
            Log.d("SEAT_API", "onCreate: $it")
            seatsAdapter.setSeatList(it)
            recyclerView.layoutManager = GridLayoutManager(this.context, seatsAdapter.getLongestRowLength())
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Log.d("SEAT_API", "onError: $it")
        })

        arguments?.getParcelable<Movie>("movie")?.let {
            viewModel.getSeatsByRoom(it.room.id)
        }
    }
}