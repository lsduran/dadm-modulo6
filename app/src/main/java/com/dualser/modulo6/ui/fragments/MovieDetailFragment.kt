package com.dualser.modulo6.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dualser.modulo6.R
import com.dualser.modulo6.application.MoviesApp
import com.dualser.modulo6.data.MovieRepository
import com.dualser.modulo6.data.db.remote.model.MovieDetailDto
import com.dualser.modulo6.databinding.FragmentMovieDetailBinding
import com.dualser.modulo6.ui.activities.TrailerActivity
import com.dualser.modulo6.utils.Constants
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

private const val MOVIE_ID = "movie_id"

class MovieDetailFragment : Fragment() {

    private var movieId: String? = null

    private var _binding: FragmentMovieDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("es", "MX"))

    private var movie: MovieDetailDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {args ->
            movieId = args.getString(MOVIE_ID)

            Log.d(Constants.LOGTAG, "ID: $movieId")

            repository = (requireActivity().application as MoviesApp).repository

            lifecycleScope.apply {
                movieId?.let { id ->
                    // val call: Call<GameDetailDto> = repository.getGameDetail(id)
                    val call: Call<MovieDetailDto> = repository.getMovieDetail(id)

                    call.enqueue(object: Callback<MovieDetailDto> {
                        override fun onResponse(
                            call: Call<MovieDetailDto>,
                            response: Response<MovieDetailDto>
                        ) {
                            movie = response.body()
                            binding.apply {
                                lavloading.visibility = View.GONE
                                tvTitle.text = movie?.title
                                rbRating.rating = movie?.rating!! / 2
                                tvGenre.text = movie?.genre
                                tvDistributor.text = movie?.distributedBy
                                tvReleased.text = getString(R.string.released_date_full, simpleDateFormat.format(movie?.releaseDate!!))
                                tvStarring.text = getString(R.string.starring_by, movie?.starring?.joinToString(", "))
                                tvDirector.text = getString(R.string.directed_by, movie?.director)
                                tvPlatform.text = getString(R.string.platforms_streaming, movie?.platforms?.joinToString(", "))
                                tvDescription.text = movie?.description

                                Glide.with(requireContext())
                                    .load(movie?.coverUrl)
                                    .into(ivImage)
                            }
                        }

                        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
                            binding.lavloading.visibility = View.GONE
                            Toast.makeText(requireActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                        }

                    })
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rlWatchTrailer?.setOnClickListener {
            val intent = Intent(requireContext(), TrailerActivity::class.java)
            intent.putExtra(Constants.EXTRA_TRAILER, movie?.trailerUrl)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(movieId: Long) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(MOVIE_ID, movieId.toString())
                }
            }
    }
}