package com.cinema.virtualcinema.ui.movies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cinema.virtualcinema.R
import com.cinema.virtualcinema.ReservationActivity
import com.cinema.virtualcinema.data.model.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private var movies = mutableListOf<Movie>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMovieList(movies: List<Movie>) {
        this.movies = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MoviesViewHolder(inflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) = holder.bind(movies[position])

    inner class MoviesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        fun bind(item: Movie) {
            view.findViewById<TextView>(R.id.movie_title).text = item.movieTitle
            view.findViewById<TextView>(R.id.movie_room).text = item.room.roomName
            view.findViewById<CardView>(R.id.movie_cardview).setOnClickListener {
                val intent = Intent(context, ReservationActivity::class.java)
                intent.putExtra("movie", item)
                context.startActivity(intent)
            }
        }
    }
}