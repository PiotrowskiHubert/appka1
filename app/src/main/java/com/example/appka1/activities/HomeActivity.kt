package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R
import com.example.appka1.models.User
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("HomeActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("HomeActivity", "Błąd: brak danych użytkownika")
        }

        val repertuarButton: Button = findViewById(R.id.btnRepertuar)
        repertuarButton.setOnClickListener {
            intent = Intent(this, ShowingsActivity::class.java).apply {
                putExtra("USER", userJson)
            }
            startActivity(intent)
        }

        val upcomingMoviesButton: Button = findViewById(R.id.btnUpcomingMovies)
        upcomingMoviesButton.setOnClickListener {
            startActivity(Intent(this, UpcomingMoviesActivity::class.java))
        }

        val myReservationsButton: Button = findViewById(R.id.btnMyReservations)
        myReservationsButton.setOnClickListener {
            intent = Intent(this, MyReservationActivity::class.java).apply {
                putExtra("USER", userJson)
            }
            startActivity(intent)
        }

        val aboutButton: Button = findViewById(R.id.btnAbout)
        aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        val promotionsButton: Button = findViewById(R.id.btnPromotions)
        promotionsButton.setOnClickListener {
            startActivity(Intent(this, PromotionsActivity::class.java))
        }

        val adminPanelButton: Button = findViewById(R.id.btnAdminPanel)
        if (user.status == 0){
            adminPanelButton.visibility = Button.VISIBLE
        }
        adminPanelButton.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java))
        }

    }
}