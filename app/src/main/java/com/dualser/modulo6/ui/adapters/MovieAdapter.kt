package com.dualser.modulo6.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.databinding.MovieItemBinding
import com.dualser.modulo6.utils.PlatformEnum

class MovieAdapter(private val onMovieClick: (MovieEntity) -> Unit): RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: List<MovieEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(movie.platformImage.id)
            .error(PlatformEnum.OTHER.id)
            .into(holder.ivIcon)

        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }

        holder.ivIcon.setOnClickListener {

        }
    }

    fun updateList(list: List<MovieEntity>) {
        movies = list
        notifyDataSetChanged()
    }

}