package com.dualser.modulo6.ui.activities

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import com.dualser.modulo6.R
import com.dualser.modulo6.databinding.ActivityTrailerBinding
import com.dualser.modulo6.utils.Constants
import java.lang.Exception

class TrailerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrailerBinding

    private var trailerUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trailerUrl = intent.getStringExtra(Constants.EXTRA_TRAILER)

        binding.vvTrailer.setVideoURI(Uri.parse(trailerUrl))

        val mc = MediaController(this)

        mc.setAnchorView(binding.vvTrailer)

        binding.vvTrailer.setMediaController(mc)

        binding.vvTrailer.start()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.vvTrailer.setOnCompletionListener {
            finish()
        }

        binding.vvTrailer.setOnPreparedListener {
            binding.pbLoading.visibility = View.GONE
        }

        binding.vvTrailer.setOnErrorListener { mediaPlayer, i, i2 ->
            Toast.makeText(this, getString(R.string.no_video_played), Toast.LENGTH_SHORT).show()
            finish()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vvTrailer.stopPlayback()
    }

    override fun onResume() {
        super.onResume()
        binding.vvTrailer.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.vvTrailer.pause()
    }
}