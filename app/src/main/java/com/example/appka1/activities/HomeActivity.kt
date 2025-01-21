package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val repertuarButton: Button = findViewById(R.id.btnRepertuar)
        repertuarButton.setOnClickListener {
            startActivity(Intent(this, ShowingsActivity::class.java))
        }

        val upcomingMoviesButton: Button = findViewById(R.id.btnUpcomingMovies)
        upcomingMoviesButton.setOnClickListener {
            startActivity(Intent(this, UpcomingMoviesActivity::class.java))
        }

        val myReservationsButton: Button = findViewById(R.id.btnMyReservations)
        myReservationsButton.setOnClickListener {
            startActivity(Intent(this, MyReservationActivity::class.java))
        }

        val aboutButton: Button = findViewById(R.id.btnAbout)
        aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        val promotionsButton: Button = findViewById(R.id.btnPromotions)
        promotionsButton.setOnClickListener {
            startActivity(Intent(this, PromotionsActivity::class.java))
        }
    }
}