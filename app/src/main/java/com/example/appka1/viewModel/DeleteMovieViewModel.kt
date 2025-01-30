package com.example.appka1.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appka1.activities.MovieDTO
import com.example.appka1.network.WroCinemaApi
import kotlinx.coroutines.launch

class DeleteMovieViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<MovieDTO>>()
    val movies: LiveData<List<MovieDTO>> = _movies

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                val movieList = WroCinemaApi.retrofitService.getAllMovies()
                _movies.postValue(movieList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteMovie(movieId: Long) {
        viewModelScope.launch {
            try {
                WroCinemaApi.retrofitService.deleteMovie(movieId)
                fetchMovies()  // Refresh list after deletion
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
