package com.example.appka1.activities.adminPanel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appka1.R
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch

class AddMovieActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_movie)

        val addMovieButton = findViewById<Button>(R.id.adminAddMovieButton)
        addMovieButton.setOnClickListener {
            lifecycleScope.launch {
                val movieTitle = findViewById<EditText>(R.id.titleEditText).text.toString()
                val movieDescription = findViewById<EditText>(R.id.descriptionEditText).text.toString()
                try {
                    WroCinemaApi.retrofitService.addMovie(
                        title = movieTitle,
                        description = movieDescription
                    )
                } catch (e: Exception) {
                    Log.e("adminAddMovie", e.toString())
                }
            }
        }

    }
}