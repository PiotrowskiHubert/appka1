package com.example.appka1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appka1.R
import com.example.appka1.models.Showing
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesActivity : AppCompatActivity() {

    private lateinit var showingsList: List<Showing>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        val moviesRecyclerView: RecyclerView = findViewById(R.id.moviesRecyclerView)
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Pobieranie danych z API
                showingsList = withContext(Dispatchers.IO) {
                    WroCinemaApi.retrofitService.getShowings()
                }

                if (showingsList.isNotEmpty()) {
                    // Grupowanie seansów po tytule filmu
                    val groupedShowings = showingsList.groupBy { it.movie.title }

                    // Ustawienie adaptera RecyclerView
                    val adapter = MoviesAdapter(this@MoviesActivity, groupedShowings)
                    moviesRecyclerView.adapter = adapter
                } else {
                    Log.e("MoviesActivity", "Brak dostępnych seansów.")
                }
            } catch (e: Exception) {
                Log.e("MoviesActivity", "Błąd pobierania filmów: ${e.message}")
            }
        }
    }
}
