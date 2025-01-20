package com.example.appka1.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R

class MyReservationActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var reservationsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myreservation)

        sharedPreferences = getSharedPreferences("Reservations", MODE_PRIVATE)
        reservationsTextView = findViewById(R.id.reservationsTextView)

        // Odczytujemy zapisane rezerwacje i wyświetlamy
        val reservedSeats = mutableListOf<String>()
        for (i in 0 until 30) {
            if (sharedPreferences.getBoolean("seat_$i", false)) {
                reservedSeats.add("Miejsce ${i + 1}")
            }
        }

        if (reservedSeats.isNotEmpty()) {
            reservationsTextView.text = "Zarezerwowane miejsca:\n" + reservedSeats.joinToString("\n")
        } else {
            reservationsTextView.text = "Brak rezerwacji."
        }
    }
}
