package com.dualser.modulo6.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dualser.modulo6.data.db.remote.model.MovieDto
import com.dualser.modulo6.databinding.MovieItemBinding

class MoviesAdapter (
    private val movies: List<MovieDto>,
    private val onMovieClicked: (MovieDto) -> Unit
): RecyclerView.Adapter<MovieDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailsViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailsViewHolder, position: Int) {
        val movie = movies[position]

        holder.bind(movie)

        // Procesar click en el elemento
        holder.itemView.setOnClickListener {
            onMovieClicked(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}