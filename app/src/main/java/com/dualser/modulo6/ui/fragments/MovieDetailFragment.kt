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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

private const val MOVIE_ID = "movie_id"

class MovieDetailFragment : Fragment(), OnMapReadyCallback {

    private var movieId: String? = null

    private var _binding: FragmentMovieDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository

    private val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("es", "MX"))

    private var movie: MovieDetailDto? = null

    // Para Google Maps
    private lateinit var map: GoogleMap

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
                            requireActivity().supportFragmentManager.popBackStack()
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

        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlWatchTrailer.setOnClickListener {
            val intent = Intent(requireContext(), TrailerActivity::class.java)
            intent.putExtra(Constants.EXTRA_TRAILER, movie?.trailerUrl)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        map.clear()
    }

    override fun onResume() {
        super.onResume()
        if(!::map.isInitialized) return
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.apply {
            isZoomGesturesEnabled = false
            isScrollGesturesEnabled = false
        }

        map.setOnMapLoadedCallback {
            createMarker(movie)
        }
    }

    private fun createMarker(movie: MovieDetailDto?) {
        binding.tvLocationNotFound.visibility = View.VISIBLE
        val coordinates = movie?.location?.let {location ->
            LatLng(location.latitude, location.longitude)
        }
        if (coordinates != null) {
            binding.tvLocationNotFound.visibility = View.GONE
            val marker = MarkerOptions()
                .position(coordinates)
                .title(movie?.distributedBy)
                .snippet(movie?.director)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.movie_marker))
            map.addMarker(marker)
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordinates, 17f),
                3000,
                null
            )
        }
    }
}