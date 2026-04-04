package com.example.themovieapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.themovieapp.model.Movie

/**
 * Camada de acesso aos filmes persistidos: delega ao [MovieDao] e expõe [LiveData] de domínio.
 */
class MoviesLocalDataSource(
    private val movieDao: MovieDao,
) {

    fun observeMovies(): LiveData<List<Movie>> =
        movieDao.observeAll().map { entities -> entities.map { it.toMovie() } }

    suspend fun saveMoviesFromApi(movies: List<Movie>) {
        val entities = movies.map { it.toEntity() }
        movieDao.deleteAll()
        movieDao.insertAll(entities)
    }

    suspend fun updateMovie(movie: Movie) {
        movieDao.update(movie.toEntity())
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.delete(movie.toEntity())
    }

    suspend fun clearAll() {
        movieDao.deleteAll()
    }

    suspend fun hasMovies(): Boolean = movieDao.count() > 0
}
