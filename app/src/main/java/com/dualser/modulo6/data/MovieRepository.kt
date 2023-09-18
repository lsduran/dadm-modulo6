package com.dualser.modulo6.data

import com.dualser.modulo6.data.db.remote.MoviesApi
import com.dualser.modulo6.data.db.remote.model.MovieDetailDto
import com.dualser.modulo6.data.db.remote.model.MovieDto
import retrofit2.Call
import retrofit2.Retrofit

class MovieRepository(private val retrofit: Retrofit) {

    private val moviesApi: MoviesApi = retrofit.create(MoviesApi::class.java)

    fun getMovies(url: String): Call<List<MovieDto>> = moviesApi.getMovies(url)

    fun getMovieDetail(id: String?): Call<MovieDetailDto> = moviesApi.getMoviwDetail(id)
}