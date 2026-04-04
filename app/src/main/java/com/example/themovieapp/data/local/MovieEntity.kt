package com.example.themovieapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,
    val title: String,
    val releaseYear: Int,
    val synopsis: String,
    val rating: Float,
    val voteCount: Int,
    val posterUrl: String?,
    val backdropUrl: String?,
    /** URLs de galeria serializadas em JSON (evita tipo complexo no Room/KSP). */
    val galleryImageUrlsJson: String,
)
