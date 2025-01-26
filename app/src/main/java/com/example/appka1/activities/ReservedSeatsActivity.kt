package com.example.appka1.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R
import com.example.appka1.models.User
import com.google.gson.Gson

class ReservedSeatsActivity : AppCompatActivity() {
    private lateinit var reservationsTextView: TextView
    private lateinit var user: User
    private var reservedSeats: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserved_seats)
        reservationsTextView = findViewById(R.id.reservedSeatsTextView)

        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("MyReservationActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("MyReservationActivity", "Błąd: brak danych użytkownika")
        }

        val showingTitle = intent.getStringExtra("SHOWING_TITLE")
        val showingStartTime = intent.getStringExtra("SHOWING_START_TIME")

        reservedSeats = intent.getStringArrayListExtra("RESERVED_SEATS") as MutableList<String>

        if (reservedSeats.isNotEmpty()) {
            reservationsTextView.text = "Zarezerwowane miejsca na seans $showingTitle o godzinie $showingStartTime:\n"
            for (seat in reservedSeats) {
                reservationsTextView.append("siedzenie nr: $seat\n")
            }
        } else {
            reservationsTextView.text = "Brak rezerwacji."
        }
    }
}
