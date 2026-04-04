package com.example.themovieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TheMovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
