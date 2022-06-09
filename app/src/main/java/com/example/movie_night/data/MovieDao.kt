package com.example.movie_night.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * from movie WHERE id = :id")
    fun getMovie(id: Int): Flow<Movie>

    @Query("SELECT * from movie ORDER BY name ASC")
    fun getMovies(): Flow<List<Movie>>
}