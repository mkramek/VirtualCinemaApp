package com.cinema.virtualcinema.ui.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.data.repository.MoviesRepository
import com.cinema.virtualcinema.data.service.MoviesService

class MoviesFragment : Fragment() {

    lateinit var viewModel: MoviesViewModel

    private val moviesService = MoviesService.getInstance()
    val moviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, MoviesViewModelFactory(MoviesRepository(moviesService))).get(MoviesViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.movies_recyclerview)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = moviesAdapter
            this.setHasFixedSize(false)
        }
        viewModel.movieList.observe(viewLifecycleOwner, Observer {
            Log.d("MOV_API", "onCreate: $it")
            moviesAdapter.setMovieList(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            Log.d("MOV_API", "onError: $it")
        })
        viewModel.getMovies()
    }
}