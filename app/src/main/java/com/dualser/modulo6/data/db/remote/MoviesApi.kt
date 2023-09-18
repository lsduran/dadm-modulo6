package com.dualser.modulo6.data.db.remote

import com.dualser.modulo6.data.db.remote.model.MovieDetailDto
import com.dualser.modulo6.data.db.remote.model.MovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MoviesApi {
    // "v1/movies"
    @GET
    fun getMovies(
        @Url url: String?
    ): Call<List<MovieDto>>

    // "v1/movies/{ID}"
    @GET("v1/movies/{id}")
    fun getMoviwDetail(
        @Path("id") id: String?
    ): Call<MovieDetailDto>
}