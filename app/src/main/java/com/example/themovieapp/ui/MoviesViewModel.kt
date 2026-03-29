package com.example.themovieapp.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapp.data.DataState
import com.example.themovieapp.model.Movie

class MoviesViewModel : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val finishListSuccess = Runnable {
        _moviesForListFragment.value = Movie.sampleList
        _applicationDataState.value = DataState.State.Success
    }

    private val finishListError = Runnable {
        _errorMessage.value = DEFAULT_LIST_ERROR
        _applicationDataState.value = DataState.State.Error
    }

    private val finishDetailLoad = Runnable {
        _movieForDetailFragment.value = pendingDetailMovie
        _applicationDataState.value = DataState.State.Success
    }

    private val _moviesForListFragment = MutableLiveData<List<Movie>>(emptyList())
    val moviesForListFragment: LiveData<List<Movie>> = _moviesForListFragment

    private val _movieForDetailFragment = MutableLiveData<Movie?>()
    val movieForDetailFragment: LiveData<Movie?> = _movieForDetailFragment

    private val _applicationDataState = MutableLiveData(DataState.State.Loading)
    val applicationDataState: LiveData<DataState.State> = _applicationDataState

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var pendingDetailMovie: Movie? = null

    init {
        loadMoviesSimulated(success = true)
    }

    fun loadMoviesSimulated(success: Boolean = true) {
        handler.removeCallbacks(finishListSuccess)
        handler.removeCallbacks(finishListError)
        _applicationDataState.value = DataState.State.Loading
        _moviesForListFragment.value = emptyList()
        if (success) {
            handler.postDelayed(finishListSuccess, LIST_DELAY_MS)
        } else {
            handler.postDelayed(finishListError, LIST_DELAY_MS)
        }
    }

    fun retryLoadMovies() {
        loadMoviesSimulated(success = true)
    }

    fun simulateListLoadError() {
        loadMoviesSimulated(success = false)
    }

    fun selectMovieForDetail(movie: Movie) {
        handler.removeCallbacks(finishListSuccess)
        handler.removeCallbacks(finishListError)
        handler.removeCallbacks(finishDetailLoad)
        pendingDetailMovie = movie
        _applicationDataState.value = DataState.State.Loading
        _movieForDetailFragment.value = null
        handler.postDelayed(finishDetailLoad, DETAIL_DELAY_MS)
    }

    /** Ao sair do fragment de detalhe (voltar): cancela carga pendente e limpa o filme. */
    fun onDetailLeft() {
        handler.removeCallbacks(finishDetailLoad)
        pendingDetailMovie = null
        _movieForDetailFragment.value = null
        if (!_moviesForListFragment.value.isNullOrEmpty()) {
            _applicationDataState.value = DataState.State.Success
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(finishListSuccess)
        handler.removeCallbacks(finishListError)
        handler.removeCallbacks(finishDetailLoad)
    }

    companion object {
        private const val LIST_DELAY_MS = 700L
        private const val DETAIL_DELAY_MS = 450L
        private const val DEFAULT_LIST_ERROR =
            "Não foi possível carregar os filmes. Verifique a conexão e tente novamente."
    }
}
