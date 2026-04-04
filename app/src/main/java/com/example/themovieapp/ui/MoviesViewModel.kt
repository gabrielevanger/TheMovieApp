package com.example.themovieapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.BuildConfig
import com.example.themovieapp.R
import com.example.themovieapp.data.DataState
import com.example.themovieapp.data.remote.TmdbRetrofit
import com.example.themovieapp.data.remote.toMovie
import com.example.themovieapp.data.remote.toTmdbPosterUrl
import com.example.themovieapp.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

/**
 * Estado exposto como [StateFlow] (fonte única) e espelhado em [LiveData] via [asLiveData]
 * para o Data Binding. Operações de rede rodam em [Dispatchers.IO]; atualizações de estado
 * voltam à Main após o [withContext]. O [viewModelScope] é cancelado com o ViewModel
 * (ligado à Activity com [androidx.fragment.app.activityViewModels]), evitando trabalho
 * após destruição do dono do ciclo de vida.
 */
class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val moviesForListFragment: LiveData<List<Movie>> = _movies.asLiveData()

    private val _detailMovie = MutableStateFlow<Movie?>(null)
    val movieForDetail: StateFlow<Movie?> = _detailMovie.asStateFlow()

    private val _applicationDataState = MutableStateFlow(DataState.State.Loading)
    val applicationDataState: LiveData<DataState.State> = _applicationDataState.asLiveData()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: LiveData<String> = _errorMessage.asLiveData()

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
            _movies.value = emptyList()
            try {
                val latest = withContext(Dispatchers.IO) {
                    TmdbRetrofit.api.getLatestMovie(BuildConfig.TMDB_API_KEY)
                }
                _movies.value = listOf(latest.toMovie())
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
        loadListJob = viewModelScope.launch {
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
            _detailMovie.value = null
            try {
                val movieId = movie.id.toInt()
                supervisorScope {
                    val detailsDef = async(Dispatchers.IO) {
                        TmdbRetrofit.api.getMovieDetails(movieId, BuildConfig.TMDB_API_KEY)
                    }
                    val imagesDef = async(Dispatchers.IO) {
                        TmdbRetrofit.api.getMovieImages(movieId, BuildConfig.TMDB_API_KEY)
                    }
                    val details = detailsDef.await()
                    val images = imagesDef.await()
                    val posterUrls = images.posters.orEmpty()
                        .mapNotNull { it.filePath.toTmdbPosterUrl() }
                        .distinct()
                    _detailMovie.value = details.toMovie(galleryImageUrls = posterUrls)
                    _applicationDataState.value = DataState.State.Success
                }
            } catch (_: Exception) {
                _detailMovie.value = movie
                _applicationDataState.value = DataState.State.Success
            }
        }
    }

    fun onDetailLeft() {
        detailJob?.cancel()
        _detailMovie.value = null
        if (_movies.value.isNotEmpty()) {
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
