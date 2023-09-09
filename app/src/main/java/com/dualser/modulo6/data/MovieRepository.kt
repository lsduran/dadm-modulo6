package com.dualser.modulo6.data

import com.dualser.modulo6.data.db.MovieDao
import com.dualser.modulo6.data.db.model.MovieEntity

class MovieRepository(private val movieDao: MovieDao) {

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun insertMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    suspend fun getAllMovies(): List<MovieEntity> = movieDao.getAllMovies()

    suspend fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: MovieEntity) {
        movieDao.deleteMovie(movie)
    }
}