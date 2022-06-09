package com.example.movie_night

import android.app.Application
import com.example.movie_night.data.MovieRoomDatabase

class MovieNightApplication : Application() {
    val database: MovieRoomDatabase by lazy {
        MovieRoomDatabase.getDatabase(this)
    }
}