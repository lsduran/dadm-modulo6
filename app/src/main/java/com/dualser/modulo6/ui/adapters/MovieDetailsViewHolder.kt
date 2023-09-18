package com.dualser.modulo6.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dualser.modulo6.R
import com.dualser.modulo6.data.db.remote.model.MovieDto
import com.dualser.modulo6.databinding.MovieItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieDetailsViewHolder(private val binding: MovieItemBinding): RecyclerView.ViewHolder(binding.root) {
    private val ivThumbnail = binding.ivThumbnail

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("es", "MX"))

    fun bind(movie: MovieDto) {
        binding.apply {
            tvTitle.text = movie.title
            tvGenre.text = movie.genre
            tvReleased.text = ivThumbnail.context.getString(R.string.released_date_full, simpleDateFormat.format(movie.releaseDate!!))
            rbRating.rating = movie.rating!! / 2
        }

        val circularProgressDrawable = CircularProgressDrawable(ivThumbnail.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        // Cargar imagen con Glide
        Glide.with(ivThumbnail.context)
            .load(movie.coverUrl)
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_error)
            .into(ivThumbnail)
    }
}