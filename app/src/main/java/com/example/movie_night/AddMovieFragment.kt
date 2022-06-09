package com.example.movie_night

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movie_night.data.Movie
import com.example.movie_night.databinding.FragmentAddMovieBinding


class AddMovieFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory(
            (activity?.application as MovieNightApplication)
                .database
                .movieDao()
        )
    }

    private val navigationArgs: AddMovieFragmentArgs by navArgs()
    lateinit var movie: Movie
    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(movie: Movie) {
        binding.apply {
            movieName.setText(movie.movieName, TextView.BufferType.SPANNABLE)
            movieYear.setText(movie.movieYear.toString(), TextView.BufferType.SPANNABLE)
            movieRating.setText(movie.movieRating.toString(), TextView.BufferType.SPANNABLE)

            saveAction.setOnClickListener { updateMovie() }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.movieName.text.toString(),
            binding.movieYear.text.toString(),
            binding.movieRating.text.toString()
        )
    }

    private fun addNewMovie() {
        if (isEntryValid()) {
            viewModel.addNewMovie(
                binding.movieName.text.toString(),
                binding.movieYear.text.toString(),
                binding.movieRating.text.toString()
            )
        }

        val action = AddMovieFragmentDirections.actionAddNewMovieToAllMoviesFragment()
        findNavController().navigate(action)
    }

    private fun updateMovie() {
        if (isEntryValid()) {
            viewModel.updateMovie(
                this.navigationArgs.movieId,
                this.binding.movieName.text.toString(),
                this.binding.movieYear.text.toString(),
                this.binding.movieRating.text.toString()
            )
            val action = AddMovieFragmentDirections.actionAddNewMovieToAllMoviesFragment()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.movieId
        if (id > 0) {
            viewModel.retrieveMovie(id).observe(this.viewLifecycleOwner) {selectedMovie ->
                movie = selectedMovie
                bind(movie)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewMovie()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }


}