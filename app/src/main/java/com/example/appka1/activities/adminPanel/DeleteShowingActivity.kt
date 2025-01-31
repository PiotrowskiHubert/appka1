package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.models.Showing
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class DeleteShowingActivity : AppCompatActivity() {

    private lateinit var dateSpinner: Spinner
    private lateinit var movieSpinner: Spinner
    private lateinit var showingsContainer: LinearLayout
    private var showingsList: List<Showing> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete_showing)

        dateSpinner = findViewById(R.id.dateSpinner)
        movieSpinner = findViewById(R.id.movieSpinner)
        showingsContainer = findViewById(R.id.showingsContainer)

        fetchShowings()
    }

    private fun fetchShowings() {
        lifecycleScope.launch {
            try {
                showingsList = WroCinemaApi.retrofitService.getShowings()
                setupDateSpinner()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupDateSpinner() {
        val dates = showingsList.map { it.date }.distinct().sorted()
        val dateAdapter = ArrayAdapter(this, R.layout.spinner_item, dates)
        dateAdapter.setDropDownViewResource(R.layout.spinner_item)
        dateSpinner.adapter = dateAdapter

        dateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDate = dates[position]
                setupMovieSpinner(selectedDate)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupMovieSpinner(selectedDate: LocalDate) {
        val movies = showingsList.filter { it.date == selectedDate }.map { it.movie.title }.distinct().sorted()
        val movieAdapter = ArrayAdapter(this, R.layout.spinner_item, movies)
        movieAdapter.setDropDownViewResource(R.layout.spinner_item)
        movieSpinner.adapter = movieAdapter

        movieSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedMovie = movies[position]
                displayShowings(selectedDate, selectedMovie)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun displayShowings(selectedDate: LocalDate, selectedMovie: String) {
        showingsContainer.removeAllViews()

        val inflater = LayoutInflater.from(this)
        val filteredShowings = showingsList.filter { it.date == selectedDate && it.movie.title == selectedMovie }.sortedBy { it.startTime }

        for (showing in filteredShowings) {
            val showingView = inflater.inflate(R.layout.item_delete_showing, showingsContainer, false)
            val titleTextView = showingView.findViewById<TextView>(R.id.showingTitle)
            val dateTextView = showingView.findViewById<TextView>(R.id.showingDate)
            val startTimeTextView = showingView.findViewById<TextView>(R.id.showingStartTime)
            val deleteButton = showingView.findViewById<Button>(R.id.deleteShowingButton)

            titleTextView.text = showing.toString()
            dateTextView.text = showing.date.toString()
            startTimeTextView.text = showing.startTime
            deleteButton.text = "Delete"
            deleteButton.setOnClickListener {
                deleteShowing(showing.id)
            }

            showingsContainer.addView(showingView)
        }
    }

    private fun deleteShowing(showingId: Long) {
        lifecycleScope.launch {
            try {
                Log.d("DeleteShowingActivity", "Showing deleted")
                WroCinemaApi.retrofitService.deleteShowing(showingId)
                fetchShowings()  // Refresh list after deletion
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}