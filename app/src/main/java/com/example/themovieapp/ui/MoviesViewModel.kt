package com.example.themovieapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.BuildConfig
import com.example.themovieapp.R
import com.example.themovieapp.data.DataState
import com.example.themovieapp.data.remote.TmdbRetrofit
import com.example.themovieapp.data.remote.toMovie
import com.example.themovieapp.data.remote.toTmdbPosterUrl
import com.example.themovieapp.model.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val _moviesForListFragment = MutableLiveData<List<Movie>>(emptyList())
    val moviesForListFragment: LiveData<List<Movie>> = _moviesForListFragment

    private val _movieForDetailFragment = MutableLiveData<Movie?>()
    val movieForDetailFragment: LiveData<Movie?> = _movieForDetailFragment

    private val _applicationDataState = MutableLiveData(DataState.State.Loading)
    val applicationDataState: LiveData<DataState.State> = _applicationDataState

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var loadListJob: Job? = null
    private var detailJob: Job? = null

    init {
        loadMoviesFromApi()
    }

    fun loadMoviesFromApi() {
        loadListJob?.cancel()
        loadListJob = viewModelScope.launch {
            if (BuildConfig.TMDB_API_KEY.isBlank()) {
                _errorMessage.value = getApplication<Application>().getString(R.string.tmdb_key_missing)
                _applicationDataState.value = DataState.State.Error
                return@launch
            }
            _applicationDataState.value = DataState.State.Loading
            _moviesForListFragment.value = emptyList()
            try {
                val latest = TmdbRetrofit.api.getLatestMovie(BuildConfig.TMDB_API_KEY)
                _moviesForListFragment.value = listOf(latest.toMovie())
                _applicationDataState.value = DataState.State.Success
            } catch (_: Exception) {
                _errorMessage.value = DEFAULT_LIST_ERROR
                _applicationDataState.value = DataState.State.Error
            }
        }
    }

    fun retryLoadMovies() {
        loadMoviesFromApi()
    }

    fun simulateListLoadError() {
        loadListJob?.cancel()
        viewModelScope.launch {
            _applicationDataState.value = DataState.State.Loading
            delay(SIMULATED_ERROR_DELAY_MS)
            _errorMessage.value = DEFAULT_LIST_ERROR
            _applicationDataState.value = DataState.State.Error
        }
    }

    fun selectMovieForDetail(movie: Movie) {
        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            if (BuildConfig.TMDB_API_KEY.isBlank()) {
                _errorMessage.value = getApplication<Application>().getString(R.string.tmdb_key_missing)
                _applicationDataState.value = DataState.State.Error
                return@launch
            }
            _applicationDataState.value = DataState.State.Loading
            _movieForDetailFragment.value = null
            try {
                val movieId = movie.id.toInt()
                coroutineScope {
                    val detailsDef = async {
                        TmdbRetrofit.api.getMovieDetails(movieId, BuildConfig.TMDB_API_KEY)
                    }
                    val imagesDef = async {
                        TmdbRetrofit.api.getMovieImages(movieId, BuildConfig.TMDB_API_KEY)
                    }
                    val details = detailsDef.await()
                    val images = imagesDef.await()
                    val posterUrls = images.posters.orEmpty()
                        .mapNotNull { it.filePath.toTmdbPosterUrl() }
                        .distinct()
                    _movieForDetailFragment.value = details.toMovie(galleryImageUrls = posterUrls)
                    _applicationDataState.value = DataState.State.Success
                }
            } catch (_: Exception) {
                _movieForDetailFragment.value = movie
                _applicationDataState.value = DataState.State.Success
            }
        }
    }

    fun onDetailLeft() {
        detailJob?.cancel()
        _movieForDetailFragment.value = null
        if (!_moviesForListFragment.value.isNullOrEmpty()) {
            _applicationDataState.value = DataState.State.Success
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadListJob?.cancel()
        detailJob?.cancel()
    }

    companion object {
        private const val SIMULATED_ERROR_DELAY_MS = 400L
        private const val DEFAULT_LIST_ERROR =
            "Não foi possível carregar os filmes. Verifique a conexão e tente novamente."
    }
}
