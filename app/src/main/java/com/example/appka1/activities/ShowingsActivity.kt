package com.example.appka1.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appka1.R
import com.example.appka1.models.Showing
import com.example.appka1.models.User
import com.example.appka1.network.WroCinemaApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class ShowingsActivity : AppCompatActivity() {
    private lateinit var showingsList: List<Showing>
    private lateinit var user: User
    private var currentDate: LocalDate = LocalDate(2025, 1, 24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
//        val date = LocalDate(2025, 1, 24)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        dateTextView.text = "${currentDate.dayOfMonth}.${currentDate.monthNumber}.${currentDate.year}"

        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("ShowingsActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("ShowingsActivity", "Błąd: brak danych użytkownika")
        }

        val moviesRecyclerView: RecyclerView = findViewById(R.id.moviesRecyclerView)
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        val leftButton: Button = findViewById(R.id.leftButton)
        val rightButton: Button = findViewById(R.id.rightButton)

        leftButton.setOnClickListener {
            currentDate = currentDate.minus(DateTimeUnit.DayBased(1))
            dateTextView.text = "${currentDate.dayOfMonth}.${currentDate.monthNumber}.${currentDate.year}"
            fetchShowings()
        }

        rightButton.setOnClickListener {
            currentDate = currentDate.plus(DateTimeUnit.DayBased(1))
            dateTextView.text = "${currentDate.dayOfMonth}.${currentDate.monthNumber}.${currentDate.year}"
            fetchShowings()
        }

        fetchShowings()
    }

    private fun fetchShowings() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                showingsList = withContext(Dispatchers.IO) {
                    WroCinemaApi.retrofitService.getShowingsByDate(
                        day = currentDate.dayOfMonth.toInt(),
                        month = currentDate.monthNumber.toInt(),
                        year = currentDate.year.toInt()
                    )
                }

//                if (showingsList.isNotEmpty()) {
                    val groupedShowings = showingsList.groupBy { it.movie.title }

                    val adapter = ShowingsAdapter(
                        this@ShowingsActivity,
                        groupedShowings,
                        user
                    )
                    val moviesRecyclerView: RecyclerView = findViewById(R.id.moviesRecyclerView)
                    moviesRecyclerView.adapter = adapter
//                } else {
//                    Log.e("MoviesActivity", "Brak dostępnych seansów.")
//                }

            } catch (e: Exception) {
                Log.e("MoviesActivity", "Błąd pobierania filmów: ${e.message}")
            }
        }
    }
}

//lifecycleScope.launch(Dispatchers.Main) {
//    try {
//        showingsList = withContext(Dispatchers.IO) {
//            WroCinemaApi.retrofitService.getShowingsByDate(
//                day = date.dayOfMonth.toInt(),
//                month = date.monthNumber.toInt(),
//                year = date.year.toInt()
//            )
//        }
//
//        if (showingsList.isNotEmpty()) {
//            val groupedShowings = showingsList.groupBy { it.movie.title }
//
//            val adapter = ShowingsAdapter(
//                this@ShowingsActivity,
//                groupedShowings,
//                user
//            )
//            moviesRecyclerView.adapter = adapter
//        } else {
//            Log.e("MoviesActivity", "Brak dostępnych seansów.")
//        }
//
//    } catch (e: Exception) {
//        Log.e("MoviesActivity", "Błąd pobierania filmów: ${e.message}")
//    }
//}