package com.dualser.modulo6.application

import android.app.Application
import com.dualser.modulo6.data.MovieRepository
import com.dualser.modulo6.data.db.MovieDatabase

class MoviesApp: Application() {
    private val database by lazy {
        MovieDatabase.getDatabase(this@MoviesApp)
    }

    val repository by lazy {
        MovieRepository(database.movieDao())
    }
}