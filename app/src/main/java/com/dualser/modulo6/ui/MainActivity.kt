package com.dualser.modulo6.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dualser.modulo6.R
import com.dualser.modulo6.application.MoviesApp
import com.dualser.modulo6.data.MovieRepository
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.databinding.ActivityMainBinding
import com.dualser.modulo6.ui.adapters.MovieAdapter
import com.dualser.modulo6.utils.SnackbarColorsEnum
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val DIALOG_TAG = "DIALOG"

    private lateinit var binding: ActivityMainBinding

    private var movies: List<MovieEntity> = emptyList()

    private lateinit var repository: MovieRepository

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as MoviesApp).repository

        movieAdapter = MovieAdapter { movie ->
            movieClicked(movie)
        }

        binding.fabAdd.setOnClickListener { view ->
            click(view)
        }

        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }

        updateUI()

    }

    private fun updateUI() {
        lifecycleScope.launch {
            movies = repository.getAllMovies()

            if(movies.isNotEmpty()) {
                binding.tvNoData.visibility = View.INVISIBLE
            } else {
                binding.tvNoData.visibility = View.VISIBLE
            }
            movieAdapter.updateList(movies)
        }
    }

    private fun click(view: View) {
        val dialog = MovieDialog(updateUI = {
            updateUI()
        }, message = { text, snackbarColor ->
            message(text, snackbarColor)
        })
        dialog.show(supportFragmentManager, DIALOG_TAG)
    }

    private fun movieClicked(movie: MovieEntity) {
        val dialog = MovieDialog(newMovie = false, movieEntity = movie, updateUI = {
            updateUI()
        }, message = {text, snackbarColor ->
            message(text, snackbarColor)
        })
        dialog.show(supportFragmentManager, DIALOG_TAG)
    }

    private fun message(text: String, snackbarColor: SnackbarColorsEnum = SnackbarColorsEnum.ACCEPT) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.white))
            .setBackgroundTint(getColor(snackbarColor.color))
            .show()
    }
}