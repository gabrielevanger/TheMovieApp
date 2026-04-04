package com.example.themovieapp.data.local

import com.example.themovieapp.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()
private val stringListType = object : TypeToken<List<String>>() {}.type

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    releaseYear = releaseYear,
    synopsis = synopsis,
    rating = rating,
    voteCount = voteCount,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    galleryImageUrlsJson = gson.toJson(galleryImageUrls),
)

fun MovieEntity.toMovie(): Movie = Movie(
    id = id,
    title = title,
    releaseYear = releaseYear,
    synopsis = synopsis,
    rating = rating,
    voteCount = voteCount,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    galleryImageUrls = gson.fromJson(galleryImageUrlsJson, stringListType) ?: emptyList(),
)
