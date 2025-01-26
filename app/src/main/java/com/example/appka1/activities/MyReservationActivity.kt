package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.models.ReservedSeats
import com.example.appka1.models.User
import com.example.appka1.network.WroCinemaApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface MyReservationResultsUiState {
    data class Success(val apiMyReservationResult: List<ReservedSeats>) : MyReservationResultsUiState
    object Error : MyReservationResultsUiState
    object Loading : MyReservationResultsUiState
}

class MyReservationActivity : AppCompatActivity() {
    private lateinit var reservationsTextView: TextView
    private lateinit var user: User
    private var myReservationApiState: MyReservationResultsUiState = MyReservationResultsUiState.Loading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myreservation)
        reservationsTextView = findViewById(R.id.reservationsTextView)

        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("MyReservationActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("MyReservationActivity", "Błąd: brak danych użytkownika")
        }

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val myReservations = withContext(Dispatchers.IO) {
                    WroCinemaApi.retrofitService.getSeatsReservedByUser(userId = user.id)
                }

                if (user != null) {
                    myReservationApiState = MyReservationResultsUiState.Success(myReservations)
                } else {
                    myReservationApiState = MyReservationResultsUiState.Error
                    Log.e("Login", "Błąd logowania: użytkownik nie istnieje")
                }

//                for (reservation in myReservations) {
//                    println(reservation.seatNumber)
//                    println(reservation.showing.movie.title)
//                }

                val reservationMap = HashMap<String, Pair<String, MutableList<Int>>>()
                for (reservation in myReservations) {
                    val title = reservation.showing.movie.title
                    val startTime = reservation.showing.startTime
                    val seatNumber = reservation.seatNumber
                    if (reservationMap.containsKey(title)) {
                        reservationMap[title]?.second?.add(seatNumber)
                    } else {
                        reservationMap[title] = Pair(startTime, mutableListOf(seatNumber))
                    }
                }

                if (reservationMap.isNotEmpty()) {
                    reservationsTextView.text = "Zarezerwowane miejsca:\n" + reservationMap.entries.joinToString("\n") {
                        "${it.key} (Start time: ${it.value.first}): ${it.value.second.joinToString(", ")}"
                    }
                } else {
                    reservationsTextView.text = "Brak rezerwacji."
                }

            } catch (e: Exception) {
                myReservationApiState = MyReservationResultsUiState.Error
                Log.e("Login", "Błąd logowania: ${e.message}")
            }
        }

    }
}
