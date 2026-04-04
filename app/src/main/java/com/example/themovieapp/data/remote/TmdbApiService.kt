package com.example.themovieapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("3/movie/latest")
    suspend fun getLatestMovie(@Query("api_key") apiKey: String): TmdbMovieJson

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "pt-BR",
    ): TmdbMovieJson

    @GET("3/movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): TmdbImagesJson
}
