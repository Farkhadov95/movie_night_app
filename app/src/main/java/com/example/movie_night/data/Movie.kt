package com.example.movie_night.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val movieName: String,
    @ColumnInfo(name = "year")
    val movieYear: Int,
    @ColumnInfo(name = "rating")
    val movieRating: Double
)