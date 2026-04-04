package com.example.themovieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TheMovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: TheMovieDatabase? = null

        fun getInstance(context: Context): TheMovieDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TheMovieDatabase::class.java,
                    "the_movie_app.db",
                ).build().also { instance = it }
            }
        }
    }
}
