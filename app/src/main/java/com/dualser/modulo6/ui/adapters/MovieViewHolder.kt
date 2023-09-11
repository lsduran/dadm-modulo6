package com.dualser.modulo6.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.databinding.MovieItemBinding
import com.dualser.modulo6.utils.PlatformEnum

class MovieViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {

    val ivIcon = binding.ivIcon

    fun bind(movie: MovieEntity) {
        binding.apply {
            tvTitle.text = movie.title
            tvGenre.text = movie.genre
            tvPlatform.text = movie.platform
            rbRating.rating = movie.rating
        }

        // Cargar imagen con Glide
        Glide.with(ivIcon.context)
            .load(movie.platformImage.id)
            .error(PlatformEnum.OTHER.id)
            .into(ivIcon)
    }
}