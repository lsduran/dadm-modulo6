package com.dualser.modulo6.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dualser.modulo6.R
import com.dualser.modulo6.application.MoviesApp
import com.dualser.modulo6.data.MovieRepository
import com.dualser.modulo6.data.db.remote.model.MovieDto
import com.dualser.modulo6.databinding.FragmentMoviesListBinding
import com.dualser.modulo6.ui.adapters.MoviesAdapter
import com.dualser.modulo6.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null

    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.zelda_secret_sound)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mediaPlayer.release()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as MoviesApp).repository

        binding.btnRefresh.setOnClickListener {
            binding.btnRefresh.visibility = View.GONE
            downloadData()
        }

        downloadData()

    }

    private fun downloadData() {
        lifecycleScope.launch {
            binding.lavloading.visibility = View.VISIBLE
            val call: Call<List<MovieDto>> = repository.getMovies(Constants.MOVIES_ENDPOINT)
            call.enqueue(object: Callback<List<MovieDto>> {
                override fun onResponse(
                    call: Call<List<MovieDto>>,
                    response: Response<List<MovieDto>>
                ) {
                    binding.lavloading.visibility = View.GONE
                    binding.btnRefresh.visibility = View.GONE
                    mediaPlayer?.start()
                    response.body()?.let { movies ->
                        binding.rvGames.apply {
                            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            adapter = MoviesAdapter(movies = movies) {movie ->

                                movie.id?.let { id ->
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, MovieDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<MovieDto>>, t: Throwable) {
                    Log.e(Constants.LOGTAG, getString(R.string.error, t.message))
                    Toast.makeText(requireActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                    binding.lavloading.visibility = View.GONE
                    binding.btnRefresh.visibility = View.VISIBLE
                }

            })
        }
    }

}