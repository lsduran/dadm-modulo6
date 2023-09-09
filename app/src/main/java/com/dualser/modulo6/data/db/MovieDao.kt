package com.dualser.modulo6.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.utils.Constants

@Dao
interface MovieDao {

    // Create
    @Insert
    suspend fun insertMovie(movie: MovieEntity)

    @Insert
    suspend fun insertMovies(movies: List<MovieEntity>)

    // Read
    @Query("select * from ${Constants.DATABASE_MOVIES_TABLE}")
    suspend fun getAllMovies(): List<MovieEntity>

    // Update
    @Update
    suspend fun updateMovie(movie: MovieEntity)

    // Delete
    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}