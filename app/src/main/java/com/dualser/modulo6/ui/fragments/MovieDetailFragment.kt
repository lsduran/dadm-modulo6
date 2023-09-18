package com.dualser.modulo6.ui.fragments

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
import com.dualser.modulo6.utils.Constants
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
                            binding.apply {
                                lavloading.visibility = View.GONE
                                tvTitle.text = response.body()?.title
                                rbRating.rating = response.body()?.rating!! / 2
                                tvGenre.text = response.body()?.genre
                                tvDistributor.text = response.body()?.distributedBy
                                tvReleased.text = getString(R.string.released_date_full, simpleDateFormat.format(response.body()?.releaseDate!!))
                                tvStarring.text = getString(R.string.starring_by, response.body()?.starring?.joinToString(", "))
                                tvDirector.text = getString(R.string.directed_by, response.body()?.director)
                                tvPlatform.text = getString(R.string.platforms_streaming, response.body()?.platforms?.joinToString(", "))
                                tvDescription.text = response.body()?.description

                                Glide.with(requireContext())
                                    .load(response.body()?.coverUrl)
                                    .into(ivImage)
                            }
                        }

                        override fun onFailure(call: Call<MovieDetailDto>, t: Throwable) {
                            binding.lavloading.visibility = View.GONE
                            Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
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