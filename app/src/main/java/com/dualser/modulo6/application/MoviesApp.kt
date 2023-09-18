package com.dualser.modulo6.application

import android.app.Application
import com.dualser.modulo6.data.MovieRepository
import com.dualser.videogamesrf.data.remote.RetrofitHelper

class MoviesApp: Application() {
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        MovieRepository(retrofit)
    }
}