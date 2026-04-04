package com.example.themovieapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentMovieListBinding
import com.example.themovieapp.model.Movie
import com.example.themovieapp.ui.MoviesViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModel()
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.toolbar.title = getString(R.string.app_display_name)
        binding.toolbar.navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_menu_24)
        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), R.string.menu_coming_soon, Toast.LENGTH_SHORT).show()
        }
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    Toast.makeText(requireContext(), R.string.search_coming_soon, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_overflow_about -> {
                    Toast.makeText(requireContext(), R.string.menu_coming_soon, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_simulate_list_error -> {
                    viewModel.simulateListLoadError()
                    true
                }
                else -> false
            }
        }

        adapter = MovieListAdapter(
            onMovieClick = { movie ->
                viewModel.selectMovieForDetail(movie)
                findNavController().navigate(R.id.action_movieListFragment_to_movieDetailFragment)
            },
            onShareClick = { movie -> shareMovie(movie) },
        )
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.adapter = adapter

        binding.buttonRetryList.setOnClickListener {
            viewModel.retryLoadMovies()
        }
    }

    private fun shareMovie(movie: Movie) {
        val text = getString(R.string.share_movie_text, movie.title, movie.synopsis)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, movie.title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_chooser_title)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
