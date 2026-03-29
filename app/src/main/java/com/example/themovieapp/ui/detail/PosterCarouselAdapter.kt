package com.example.themovieapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themovieapp.databinding.ItemPosterStripBinding

class PosterCarouselAdapter : RecyclerView.Adapter<PosterCarouselAdapter.PosterViewHolder>() {

    private val resIds = mutableListOf<Int>()

    fun submitList(newIds: List<Int>) {
        resIds.clear()
        resIds.addAll(newIds)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val binding = ItemPosterStripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        holder.bind(resIds[position])
    }

    override fun getItemCount() = resIds.size

    class PosterViewHolder(private val binding: ItemPosterStripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resId: Int) {
            binding.imagePoster.setImageResource(resId)
        }
    }
}
