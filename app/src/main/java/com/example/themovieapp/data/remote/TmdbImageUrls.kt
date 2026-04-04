package com.example.themovieapp.data.remote

private const val POSTER_BASE = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE = "https://image.tmdb.org/t/p/w780"

fun String?.toTmdbPosterUrl(): String? =
    this?.takeIf { it.isNotBlank() }?.let { POSTER_BASE + it }

fun String?.toTmdbBackdropUrl(): String? =
    this?.takeIf { it.isNotBlank() }?.let { BACKDROP_BASE + it }

fun TmdbMovieJson.toMovie(galleryImageUrls: List<String> = emptyList()): com.example.themovieapp.model.Movie {
    val year = releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    return com.example.themovieapp.model.Movie(
        id = id.toString(),
        title = title.ifBlank { "Sem título" },
        releaseYear = year,
        synopsis = overview.orEmpty().ifBlank {
            "Não há sinopse cadastrada no TMDB para este título."
        },
        rating = voteAverage.toFloat().coerceIn(0f, 10f),
        voteCount = voteCount,
        posterUrl = posterPath.toTmdbPosterUrl(),
        backdropUrl = backdropPath.toTmdbBackdropUrl(),
        galleryImageUrls = galleryImageUrls,
    )
}
