package com.example.themovieapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.themovieapp.TestTheMovieApplication
import com.example.themovieapp.data.DataState
import com.example.themovieapp.data.remote.TmdbApiService
import com.example.themovieapp.data.repository.MovieListRefreshResult
import com.example.themovieapp.data.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], application = TestTheMovieApplication::class)
class MoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun lastUiStateAfterIdle(
        vm: MoviesViewModel,
        advance: () -> Unit,
    ): DataState.State? {
        var last: DataState.State? = null
        vm.applicationDataState.observeForever { last = it }
        advance()
        return last
    }

    @Test
    fun init_whenRepositoryReturnsNetworkSuccess_setsApplicationStateSuccess() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repo = mockk<MoviesRepository>()
        val api = mockk<TmdbApiService>(relaxed = true)
        coEvery { repo.observeMovies() } returns MutableLiveData(emptyList())
        coEvery { repo.refreshMovieList(any()) } returns MovieListRefreshResult.NetworkSuccess

        val vm = MoviesViewModel(RuntimeEnvironment.getApplication(), repo, api)
        val state = lastUiStateAfterIdle(vm) { advanceUntilIdle() }

        assertEquals(DataState.State.Success, state)
    }

    @Test
    fun init_whenRepositoryReturnsCacheUsed_setsApplicationStateSuccess() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repo = mockk<MoviesRepository>()
        val api = mockk<TmdbApiService>(relaxed = true)
        coEvery { repo.observeMovies() } returns MutableLiveData(emptyList())
        coEvery { repo.refreshMovieList(any()) } returns MovieListRefreshResult.CacheUsed

        val vm = MoviesViewModel(RuntimeEnvironment.getApplication(), repo, api)
        val state = lastUiStateAfterIdle(vm) { advanceUntilIdle() }

        assertEquals(DataState.State.Success, state)
    }

    @Test
    fun init_whenRepositoryReturnsNetworkFailure_setsApplicationStateError() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repo = mockk<MoviesRepository>()
        val api = mockk<TmdbApiService>(relaxed = true)
        coEvery { repo.observeMovies() } returns MutableLiveData(emptyList())
        coEvery { repo.refreshMovieList(any()) } returns MovieListRefreshResult.NetworkFailure

        val vm = MoviesViewModel(RuntimeEnvironment.getApplication(), repo, api)
        val state = lastUiStateAfterIdle(vm) { advanceUntilIdle() }

        assertEquals(DataState.State.Error, state)
    }

    @Test
    fun init_whenRepositoryReturnsApiKeyMissing_setsApplicationStateError() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val repo = mockk<MoviesRepository>()
        val api = mockk<TmdbApiService>(relaxed = true)
        coEvery { repo.observeMovies() } returns MutableLiveData(emptyList())
        coEvery { repo.refreshMovieList(any()) } returns MovieListRefreshResult.ApiKeyMissing

        val vm = MoviesViewModel(RuntimeEnvironment.getApplication(), repo, api)
        val state = lastUiStateAfterIdle(vm) { advanceUntilIdle() }

        assertEquals(DataState.State.Error, state)
    }
}
