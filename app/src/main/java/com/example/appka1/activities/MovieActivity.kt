package com.example.appka1.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.models.Movie
import com.example.appka1.models.Seat
import com.example.appka1.models.User
import com.example.appka1.network.WroCinemaApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface MovieResultsUiState {
    data class Success(val movieApiResult: Movie) : MovieResultsUiState
    object Error : MovieResultsUiState
    object Loading : MovieResultsUiState
}

class MovieActivity : AppCompatActivity(){
    private lateinit var user: User
    private lateinit var movie: Movie
    private var movieApiState: MovieResultsUiState = MovieResultsUiState.Loading

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieTitle = intent.getStringExtra("MOVIE_TITLE")
        val userJson = intent.getStringExtra("USER")
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
            Log.d("HomeActivity", "Odebrano użytkownika: $user")
        } else {
            Log.e("HomeActivity", "Błąd: brak danych użytkownika")
        }

        val movieTitleView: TextView = findViewById(R.id.movieTitle)
        val movieDescriptionView: TextView = findViewById(R.id.movieDescription)
        var moviePosterView = findViewById<ImageView>(R.id.moviePoster)

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                movie = withContext(Dispatchers.IO) {
                    WroCinemaApi.retrofitService.findMovieByTitle(title = movieTitle!!)
                }

                if (movie != null) {
                    movieApiState = MovieResultsUiState.Success(movie)
                } else {
                    movieApiState = MovieResultsUiState.Error
                }

            }catch (e: Exception) {
                movieApiState = MovieResultsUiState.Error
            }

            if (movieApiState is MovieResultsUiState.Success) {
                movieTitleView.text = movie.title
                movieDescriptionView.text = movie.description
                loadMoviePoster(movie.id, moviePosterView)
            } else {
                movieTitleView.text = "Error"
                movieDescriptionView.text = "Error"
            }
        }
    }

    private fun loadMoviePoster(movieId: Long, imageView: ImageView) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = WroCinemaApi.retrofitService.getMoviePoster(movieId).execute()

                if (response.isSuccessful && response.body() != null) {
                    val inputStream = response.body()!!.byteStream()
                    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)

                    withContext(Dispatchers.Main) {
                        if (!isFinishing && !isDestroyed) {
                            imageView.setImageBitmap(bitmap)
                        }
                    }
                } else {
                    Log.e("MovieActivity", "Błąd pobierania obrazu: ${response.code()} ${response.message()}")
                    withContext(Dispatchers.Main) {
                        if (!isFinishing && !isDestroyed) {
                            imageView.setImageResource(R.drawable.image1) // Set a placeholder image
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MovieActivity", "Błąd pobierania plakatu", e)
                withContext(Dispatchers.Main) {
                    if (!isFinishing && !isDestroyed) {
                        imageView.setImageResource(R.drawable.image1) // Set a placeholder image
                    }
                }
            }
        }
    }
}