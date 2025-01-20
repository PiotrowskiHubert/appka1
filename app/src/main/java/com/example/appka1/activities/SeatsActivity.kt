package com.example.appka1.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appka1.R

class SeatsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seats)

        sharedPreferences = getSharedPreferences("Reservations", MODE_PRIVATE)
        val seatGrid: GridLayout = findViewById(R.id.seat_grid)
        val confirmButton: Button = findViewById(R.id.confirm_button)

        val seatStatus = MutableList(30) { i -> sharedPreferences.getBoolean("seat_$i", false) }

        seatStatus.forEachIndexed { i, isReserved ->
            val seatButton = Button(this).apply {
                text = (i + 1).toString()
                setBackgroundColor(getColor(if (isReserved) android.R.color.holo_green_light else android.R.color.darker_gray))

                setOnClickListener {
                    val newStatus = !seatStatus[i]
                    seatStatus[i] = newStatus
                    setBackgroundColor(getColor(if (newStatus) android.R.color.holo_green_light else android.R.color.darker_gray))
                }
            }
            seatGrid.addView(seatButton)
        }

        confirmButton.setOnClickListener {
            if (seatStatus.any { it }) {
                sharedPreferences.edit().apply {
                    seatStatus.forEachIndexed { i, isReserved -> putBoolean("seat_$i", isReserved) }
                    apply()
                }
                Toast.makeText(this, "Zarezerwowano ${seatStatus.count { it }} miejsc!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MyReservationActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Wybierz przynajmniej jedno miejsce!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}