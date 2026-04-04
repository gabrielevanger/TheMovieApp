package com.example.themovieapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.themovieapp.R
import com.example.themovieapp.databinding.FragmentMovieDetailBinding
import com.example.themovieapp.ui.MoviesViewModel

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val navController = findNavController()
        binding.toolbar.title = getString(R.string.app_display_name)
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.inflateMenu(R.menu.menu_detail)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_detail_more) {
                Toast.makeText(requireContext(), R.string.menu_coming_soon, Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }

        binding.recyclerPosters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val stripAdapter = PosterCarouselAdapter()
        binding.recyclerPosters.adapter = stripAdapter

        viewModel.movieForDetailFragment.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                binding.textHeroOverlay.text = movie.title
                binding.textTitleWithYear.text = movie.titleWithYear()
                binding.textSynopsis.text = movie.synopsis
                binding.textRating.text = if (movie.voteCount > 0) {
                    getString(R.string.rating_percent_format, movie.ratingPercent())
                } else {
                    getString(R.string.rating_unavailable)
                }
                binding.imageHero.load(movie.heroImageUrl()) {
                    crossfade(true)
                    placeholder(android.R.color.darker_gray)
                    error(android.R.color.darker_gray)
                }
                stripAdapter.submitImageUrls(movie.galleryImageUrls)
                val hasPosters = movie.galleryImageUrls.isNotEmpty()
                binding.recyclerPosters.isVisible = hasPosters
                binding.textPostersEmpty.isVisible = !hasPosters
                if (!hasPosters) {
                    binding.textPostersEmpty.text = getString(R.string.posters_empty_tmdb)
                }
            } else {
                binding.textHeroOverlay.text = ""
                binding.textTitleWithYear.text = ""
                binding.textSynopsis.text = ""
                binding.textRating.text = ""
                stripAdapter.submitImageUrls(emptyList())
                binding.recyclerPosters.isVisible = false
                binding.textPostersEmpty.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        if (!requireActivity().isChangingConfigurations) {
            viewModel.onDetailLeft()
        }
        super.onDestroyView()
        _binding = null
    }
}
