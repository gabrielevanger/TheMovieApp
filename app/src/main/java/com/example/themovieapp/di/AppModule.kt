package com.example.themovieapp.di

import androidx.room.Room
import com.example.themovieapp.data.local.MovieDao
import com.example.themovieapp.data.local.MoviesLocalDataSource
import com.example.themovieapp.data.local.TheMovieDatabase
import com.example.themovieapp.data.remote.MoviesRemoteDataSource
import com.example.themovieapp.data.remote.TmdbApiService
import com.example.themovieapp.data.repository.MoviesRepository
import com.example.themovieapp.ui.MoviesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BASE_URL = "https://api.themoviedb.org/"

val appModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<TmdbApiService> { get<Retrofit>().create(TmdbApiService::class.java) }

    single<TheMovieDatabase> {
        Room.databaseBuilder(
            androidContext(),
            TheMovieDatabase::class.java,
            "the_movie_app.db",
        ).build()
    }
    single<MovieDao> { get<TheMovieDatabase>().movieDao() }

    single { MoviesLocalDataSource(get()) }
    single { MoviesRemoteDataSource(get()) }
    single { MoviesRepository(get(), get()) }

    viewModelOf(::MoviesViewModel)
}
