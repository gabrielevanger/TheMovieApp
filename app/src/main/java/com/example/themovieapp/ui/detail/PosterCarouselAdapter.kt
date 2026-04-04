package com.example.themovieapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.themovieapp.databinding.ItemPosterStripBinding

class PosterCarouselAdapter : RecyclerView.Adapter<PosterCarouselAdapter.PosterViewHolder>() {

    private val urls = mutableListOf<String>()

    fun submitImageUrls(newUrls: List<String>) {
        urls.clear()
        urls.addAll(newUrls)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val binding = ItemPosterStripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        holder.bind(urls[position])
    }

    override fun getItemCount() = urls.size

    class PosterViewHolder(private val binding: ItemPosterStripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            binding.imagePoster.load(url) {
                crossfade(true)
                placeholder(android.R.color.darker_gray)
                error(android.R.color.darker_gray)
            }
        }
    }
}
