package com.cinema.virtualcinema

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cinema.virtualcinema.data.model.Movie
import com.cinema.virtualcinema.ui.reservationseats.ReservationSeatsFragment

class ReservationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.title = getString(R.string.reservation_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_reservation)
        if (savedInstanceState == null) {
            val movie: Movie? = intent.getParcelableExtra("movie")
            val bundle: Bundle = Bundle()
            bundle.putParcelable("movie", movie!!)
            val reservationFragment: Fragment = ReservationSeatsFragment.newInstance()
            reservationFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, reservationFragment)
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }
}
