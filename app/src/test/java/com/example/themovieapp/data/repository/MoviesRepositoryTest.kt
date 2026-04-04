package com.example.themovieapp.data.repository

import com.example.themovieapp.data.local.MoviesLocalDataSource
import com.example.themovieapp.data.remote.MoviesRemoteDataSource
import com.example.themovieapp.model.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MoviesRepositoryTest {

    private lateinit var remote: MoviesRemoteDataSource
    private lateinit var local: MoviesLocalDataSource
    private lateinit var repository: MoviesRepository

    private val sampleMovie = Movie(
        id = "1",
        title = "Test",
        releaseYear = 2024,
        synopsis = "Synopsis",
        rating = 8f,
        posterUrl = null,
        backdropUrl = null,
        galleryImageUrls = emptyList(),
    )

    @Before
    fun setUp() {
        remote = mockk()
        local = mockk(relaxed = true)
        repository = MoviesRepository(remote, local)
    }

    @Test
    fun refreshMovieList_blankApiKey_returnsApiKeyMissing() = runBlocking {
        assertEquals(MovieListRefreshResult.ApiKeyMissing, repository.refreshMovieList(""))
        assertEquals(MovieListRefreshResult.ApiKeyMissing, repository.refreshMovieList("   "))
    }

    @Test
    fun refreshMovieList_networkSuccess_persistsAndReturnsNetworkSuccess() = runBlocking {
        val movies = listOf(sampleMovie)
        coEvery { remote.fetchLatestMovies("key") } returns movies
        assertEquals(MovieListRefreshResult.NetworkSuccess, repository.refreshMovieList("key"))
        coVerify(exactly = 1) { local.saveMoviesFromApi(movies) }
    }

    @Test
    fun refreshMovieList_networkFails_whenHasLocalData_returnsCacheUsed() = runBlocking {
        coEvery { remote.fetchLatestMovies("key") } throws IOException("offline")
        coEvery { local.hasMovies() } returns true
        assertEquals(MovieListRefreshResult.CacheUsed, repository.refreshMovieList("key"))
        coVerify(exactly = 0) { local.saveMoviesFromApi(match { true }) }
    }

    @Test
    fun refreshMovieList_networkFails_whenEmptyLocal_returnsNetworkFailure() = runBlocking {
        coEvery { remote.fetchLatestMovies("key") } throws IOException("offline")
        coEvery { local.hasMovies() } returns false
        assertEquals(MovieListRefreshResult.NetworkFailure, repository.refreshMovieList("key"))
    }

    @Test
    fun hasCachedMovies_delegatesToLocal() = runBlocking {
        coEvery { local.hasMovies() } returns true
        assertEquals(true, repository.hasCachedMovies())
        coEvery { local.hasMovies() } returns false
        assertEquals(false, repository.hasCachedMovies())
    }
}
