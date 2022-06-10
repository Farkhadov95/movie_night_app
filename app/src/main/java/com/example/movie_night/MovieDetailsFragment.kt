package com.example.movie_night

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movie_night.data.Movie
import com.example.movie_night.databinding.FragmentAllMoviesBinding
import com.example.movie_night.databinding.FragmentMovieDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory(
            (activity?.application as MovieNightApplication).database.movieDao()
        )
    }

    private val navigationArgs: MovieDetailsFragmentArgs by navArgs()
    lateinit var movie: Movie
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private fun bind(movie: Movie) {
        binding.apply {
            movieName.text = movie.movieName
            movieYear.text = movie.movieYear.toString()
            movieRating.text = movie.movieRating.toString()

            decreaseRating.isEnabled = viewModel.isRatingAvailable(movie)
            increaseRating.isEnabled = viewModel.isRatingAvailable(movie)

            decreaseRating.setOnClickListener {
                viewModel.decreaseRating(movie)
            }

            increaseRating.setOnClickListener {
                viewModel.increaseRating(movie)
            }

            deleteMovie.setOnClickListener {
                showConfirmationDialog()
            }
            editMovie.setOnClickListener {
                editMovie()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.movieId
        viewModel.retrieveMovie(id).observe(this.viewLifecycleOwner) {selectedMovie ->
            movie = selectedMovie
            bind(movie)
        }
    }

    private fun deleteMovie() {
        viewModel.deleteMovie(movie)
        findNavController().navigateUp()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteMovie()
            }
            .show()
    }

    private fun editMovie() {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToAddNewMovie(
            getString(R.string.edit_fragment_title),
            movie.id
        )
        this.findNavController().navigate(action)
    }

}