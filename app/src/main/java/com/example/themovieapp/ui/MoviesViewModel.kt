package com.example.themovieapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.model.Movie

class MoviesViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>(Movie.sampleList)
    val movies: LiveData<List<Movie>> = _movies

    private val _selectedMovie = MutableLiveData<Movie?>()
    val selectedMovie: LiveData<Movie?> = _selectedMovie

    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
    }
}
