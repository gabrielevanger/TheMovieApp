package com.example.themovieapp.data.remote

import com.google.gson.annotations.SerializedName

data class TmdbMovieJson(
    val id: Int,
    val title: String,
    val overview: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int = 0,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
)

data class TmdbImagesJson(
    val id: Int,
    val posters: List<TmdbImageFileJson>?,
)

data class TmdbImageFileJson(
    @SerializedName("file_path") val filePath: String,
)
