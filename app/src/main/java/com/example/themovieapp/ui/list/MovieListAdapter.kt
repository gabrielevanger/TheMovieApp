package com.example.themovieapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.databinding.ItemMovieCardBinding
import com.example.themovieapp.model.Movie

class MovieListAdapter(
    private val onMovieClick: (Movie) -> Unit,
    private val onShareClick: (Movie) -> Unit,
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onMovieClick, onShareClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(
        private val binding: ItemMovieCardBinding,
        private val onMovieClick: (Movie) -> Unit,
        private val onShareClick: (Movie) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.executePendingBindings()
            binding.buttonDetails.setOnClickListener { onMovieClick(movie) }
            binding.buttonShare.setOnClickListener { onShareClick(movie) }
            binding.imagePoster.setOnClickListener { onMovieClick(movie) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}
