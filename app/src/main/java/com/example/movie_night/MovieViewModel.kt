package com.example.movie_night

import androidx.lifecycle.*
import com.example.movie_night.data.Movie
import com.example.movie_night.data.MovieDao
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {

    val allMovies: LiveData<List<Movie>> = movieDao.getMovies().asLiveData()
    private fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            movieDao.insert(movie)
        }
    }

    private fun getNewMovieEntry(
        movieName: String,
        movieYear: String,
        movieRating: String
    ): Movie {
        return Movie(
            movieName = movieName,
            movieYear = movieYear.toInt(),
            movieRating = movieRating.toDouble()
        )
    }

    fun addNewMovie(movieName: String, movieYear: String, movieRating: String) {
        val newMovie = getNewMovieEntry(movieName, movieYear, movieRating)
        insertMovie(newMovie)
    }

    fun isEntryValid(movieName: String, movieYear: String, movieRating: String): Boolean {
        if (movieName.isBlank() || movieYear.isBlank() || movieRating.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveMovie(id: Int): LiveData<Movie> {
        return movieDao.getMovie(id).asLiveData()
    }

    private fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            movieDao.update(movie)
        }
    }

    fun decreaseRating(movie: Movie) {
        if (movie.movieRating > 0) {
            val newRating = movie.copy(
                movieRating = (((movie.movieRating - 0.1) * 100.0).roundToInt() /100.0)
            )
            updateMovie(newRating)
        }
    }

    fun increaseRating(movie: Movie) {
        if (movie.movieRating < 10) {
            val newRating = movie.copy(
                movieRating = (((movie.movieRating + 0.1) * 100.0).roundToInt() /100.0)
            )
            updateMovie(newRating)
        }
    }

    fun isRatingAvailable(movie: Movie): Boolean {
        return (movie.movieRating > 0 || movie.movieRating < 10)
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieDao.delete(movie)
        }
    }

    private fun getUpdatedMovieEntry(
        movieId: Int,
        movieName:String,
        movieYear: String,
        movieRating: String
    ): Movie {
        return Movie(
            id = movieId,
            movieName = movieName,
            movieYear = movieYear.toInt(),
            movieRating = movieRating.toDouble()
        )
    }

    fun updateMovie(
        movieId: Int,
        movieName: String,
        movieYear: String,
        movieRating: String
    ) {
        val updatedMovie = getUpdatedMovieEntry(movieId, movieName, movieYear, movieRating)
        updateMovie(updatedMovie)
    }

}

class MovieViewModelFactory(private val movieDao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}