package com.example.themovieapp.data.repository

import androidx.lifecycle.LiveData
import com.example.themovieapp.data.local.MoviesLocalDataSource
import com.example.themovieapp.data.remote.MoviesRemoteDataSource
import com.example.themovieapp.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Padrão repositório: unifica rede (Retrofit) e persistência (Room).
 * A UI observa [observeMovies] (LiveData do cache local). [refreshMovieList] tenta a API primeiro;
 * se falhar e houver dados locais, mantém sucesso com cache; caso contrário sinaliza falha.
 */
class MoviesRepository(
    private val remote: MoviesRemoteDataSource,
    private val local: MoviesLocalDataSource,
) {

    fun observeMovies(): LiveData<List<Movie>> = local.observeMovies()

    suspend fun refreshMovieList(apiKey: String): MovieListRefreshResult = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) return@withContext MovieListRefreshResult.ApiKeyMissing
        try {
            val movies = remote.fetchLatestMovies(apiKey)
            local.saveMoviesFromApi(movies)
            MovieListRefreshResult.NetworkSuccess
        } catch (_: Exception) {
            if (local.hasMovies()) {
                MovieListRefreshResult.CacheUsed
            } else {
                MovieListRefreshResult.NetworkFailure
            }
        }
    }

    suspend fun hasCachedMovies(): Boolean = withContext(Dispatchers.IO) {
        local.hasMovies()
    }
}

sealed class MovieListRefreshResult {
    data object NetworkSuccess : MovieListRefreshResult()
    data object CacheUsed : MovieListRefreshResult()
    data object NetworkFailure : MovieListRefreshResult()
    data object ApiKeyMissing : MovieListRefreshResult()
}
