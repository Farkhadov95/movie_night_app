package com.example.movie_night

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movie_night.databinding.FragmentAllMoviesBinding


class MovieListFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels {
        MovieViewModelFactory(
            (activity?.application as MovieNightApplication).database.movieDao()
        )
    }

    private var _binding: FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieListAdapter {
            val action = MovieListFragmentDirections.actionAllMoviesFragmentToMovieDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter
        viewModel.allMovies.observe(this.viewLifecycleOwner) {movies ->
            movies.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.FABAddNewMovie.setOnClickListener {
            val action = MovieListFragmentDirections.actionAllMoviesFragmentToAddNewMovie(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
    }

}