package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.appka1.R
import com.example.appka1.activities.MovieDTO
import com.example.appka1.viewModel.DeleteMovieViewModel

class DeleteMovieActivity : AppCompatActivity() {

    private lateinit var moviesContainer: LinearLayout
    private lateinit var viewModel: DeleteMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_delete_movie)

        moviesContainer = findViewById(R.id.moviesContainer)
        viewModel = ViewModelProvider(this)[DeleteMovieViewModel::class.java]

        viewModel.movies.observe(this) { movies ->
            displayMovies(movies)
        }

        viewModel.fetchMovies()
    }

    private fun displayMovies(movies: List<MovieDTO>) {
        moviesContainer.removeAllViews()

        if (movies.isEmpty()) {
            println("No movies found. UI should be empty.")
        }

        val inflater = LayoutInflater.from(this)
        for (movie in movies) {
            val movieView = inflater.inflate(R.layout.item_delete_movie, moviesContainer, false)
            val titleTextView = movieView.findViewById<TextView>(R.id.movieTitle)
            val deleteButton = movieView.findViewById<Button>(R.id.deleteMovieButton)

            titleTextView.text = movie.title
            deleteButton.setOnClickListener {
                viewModel.deleteMovie(movie.id)
            }

            moviesContainer.addView(movieView)
        }

        // 🚀 Force ScrollView to refresh its child views
        moviesContainer.invalidate()
        moviesContainer.requestLayout()
    }

}
