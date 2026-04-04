package com.example.themovieapp.data.remote

import com.example.themovieapp.model.Movie

/**
 * Origem remota da listagem: apenas TMDB via Retrofit.
 */
class MoviesRemoteDataSource(
    private val api: TmdbApiService,
) {

    suspend fun fetchLatestMovies(apiKey: String): List<Movie> {
        val latest = api.getLatestMovie(apiKey)
        return listOf(latest.toMovie())
    }
}
