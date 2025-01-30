package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddShowingActivity : AppCompatActivity() {

    private lateinit var movieTitleSpinner: Spinner
    private lateinit var screeningRoomSpinner: Spinner
    private lateinit var startTimeSpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var addShowingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_showing)

        movieTitleSpinner = findViewById(R.id.movieTitleSpinner)
        screeningRoomSpinner = findViewById(R.id.screeningRoomSpinner)
        startTimeSpinner = findViewById(R.id.startTimeSpinner)
        dateEditText = findViewById(R.id.dateEditText)
        addShowingButton = findViewById(R.id.adminAddShowingButton)

        fetchMovies()
        fetchScreeningRooms()
        setupStartTimeSpinner()

        addShowingButton.setOnClickListener {
            addShowing()
        }
    }

    private fun fetchMovies() {
        lifecycleScope.launch {
            try {
                val movies = WroCinemaApi.retrofitService.getAllMovies()
                val movieTitles = movies.map { it.title }
                val adapter = ArrayAdapter(this@AddShowingActivity, R.layout.spinner_item, movieTitles)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                movieTitleSpinner.adapter = adapter
            } catch (e: Exception) {
                Log.e("AddShowingActivity", "Error fetching movies: ${e.message}")
            }
        }
    }

    private fun fetchScreeningRooms() {
        lifecycleScope.launch {
            try {
                val rooms = WroCinemaApi.retrofitService.getAllScreeningRooms()
                val roomNames = rooms.map { it.name }
                val adapter = ArrayAdapter(this@AddShowingActivity, R.layout.spinner_item, roomNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                screeningRoomSpinner.adapter = adapter
            } catch (e: Exception) {
                Log.e("AddShowingActivity", "Error fetching screening rooms: ${e.message}")
            }
        }
    }

    private fun setupStartTimeSpinner() {
        val startTimes = listOf("10:00", "12:00", "14:00", "16:00", "18:00", "20:00")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, startTimes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startTimeSpinner.adapter = adapter
    }
    private fun addShowing() {
        val movieTitle = movieTitleSpinner.selectedItem.toString()
        val screeningRoomName = screeningRoomSpinner.selectedItem.toString()
        val startTime = startTimeSpinner.selectedItem.toString()
        val date = LocalDate.parse(dateEditText.text.toString())

        lifecycleScope.launch {
            try {
                WroCinemaApi.retrofitService.addShowing(
                    movieTitle = movieTitle,
                    screeningRoomName = screeningRoomName,
                    startTime = startTime,
                    date = date
                )
            } catch (e: Exception) {
                Log.e("AddShowingActivity", "Error adding showing: ${e.message}")
            }
        }
    }
}