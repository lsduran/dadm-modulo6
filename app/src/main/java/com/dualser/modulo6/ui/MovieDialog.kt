package com.dualser.modulo6.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dualser.modulo6.R
import com.dualser.modulo6.application.MoviesApp
import com.dualser.modulo6.data.MovieRepository
import com.dualser.modulo6.data.db.model.MovieEntity
import com.dualser.modulo6.databinding.MovieDialogBinding
import com.dualser.modulo6.utils.PlatformEnum
import com.dualser.modulo6.utils.SnackbarColorsEnum
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDialog(
    private val newMovie: Boolean = true,
    private var movieEntity: MovieEntity = MovieEntity(
        title = "",
        genre = "",
        platform = "",
        platformImage = PlatformEnum.OTHER,
        rating = 5.0F,
    ),
    private val updateUI: () -> Unit,
    private val message: (String, SnackbarColorsEnum) -> Unit
) : DialogFragment() {

    private var _binding: MovieDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder

    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: MovieRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = MovieDialogBinding.inflate(requireActivity().layoutInflater)
        repository = (requireContext().applicationContext as MoviesApp).repository
        builder = AlertDialog.Builder(requireContext())

        binding.apply {
            tietTitle.setText(movieEntity.title)
            tietGenre.setText(movieEntity.genre)
            actvPlatform.setText(movieEntity.platform)
            sRating.value = movieEntity.rating
        }

        Glide.with(binding.ivHeader.context)
            .load(movieEntity.platformImage.id)
            .error(PlatformEnum.OTHER.id)
            .into(binding.ivHeader)

        dialog = if (newMovie) {
            buildDialog(getString(R.string.save), getString(R.string.cancel), { } , {
                //Cancelar
                message(getString(R.string.action_cancelled), SnackbarColorsEnum.WARN)
            })
        } else {
            buildDialog(getString(R.string.update), getString(R.string.delete), { }, {
                val deletedMessage = getString(R.string.movie_deleted, movieEntity.title)
                val notDeletedMessage = getString(R.string.movie_not_deleted, movieEntity.title)
                // Borrar (Delete)
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_confirmation, movieEntity.title))
                    .setPositiveButton(getString(R.string.accept)) { _,_ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteMovie(movieEntity)
                            }
                            message(deletedMessage, SnackbarColorsEnum.OK)
                            updateUI()

                        } catch (e: IOException) {
                            e.printStackTrace()
                            message(notDeletedMessage, SnackbarColorsEnum.DANGER)
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _,_ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        val alertDialog = dialog as AlertDialog
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)



        return dialog
    }

    override fun onResume() {
        super.onResume()
        val platforms = resources.getStringArray(R.array.platforms)
        val platformsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_platform_item, platforms)
        binding.actvPlatform.setAdapter(platformsAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog = dialog as AlertDialog // Se usa para poder emplear el método getButton (no lo incluye el dialog)
        saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = true

        saveButton?.setOnClickListener(object: OnClickListener {
            override fun onClick(p0: View?) {
                if(newMovie) {
                    // Guardar (create)
                    try {
                        if(!validateFields()) return

                        binding.apply {
                            movieEntity.title = tietTitle.text.toString()
                            movieEntity.genre = tietGenre.text.toString()
                            movieEntity.platform = actvPlatform.text.toString()
                            movieEntity.rating = sRating.value
                        }

                        // Se sugiere agregar un dispatcher como argumento
                        // Por default selecciona el más adecuado
                        lifecycleScope.launch {
                            repository.insertMovie(movieEntity)
                        }
                        message(getString(R.string.movie_created, binding.tietTitle.text.toString()), SnackbarColorsEnum.OK)
                        updateUI()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        message(getString(R.string.movie_not_created, binding.tietTitle.text.toString()), SnackbarColorsEnum.DANGER)
                    }
                    dismiss()
                } else {
                    // Actualizar (Update)
                    try {
                        if(!validateFields()) return
                        binding.apply {
                            movieEntity.title = tietTitle.text.toString()
                            movieEntity.genre = tietGenre.text.toString()
                            movieEntity.platform = actvPlatform.text.toString()
                            movieEntity.rating = sRating.value
                        }
                        lifecycleScope.launch {
                            repository.updateMovie(movieEntity)
                        }
                        message(getString(R.string.movie_updated, binding.tietTitle.text.toString()), SnackbarColorsEnum.OK)
                        updateUI()

                    } catch (e: IOException) {
                        e.printStackTrace()
                        message(getString(R.string.movie_not_updated, binding.tietTitle.text.toString()), SnackbarColorsEnum.DANGER)
                    }
                    dismiss()
                }
            }

        })

        binding.tietTitle.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietGenre.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // saveButton?.isEnabled = validateFields()
            }

        })

        binding.actvPlatform.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // saveButton?.isEnabled = validateFields()
                val platform = when (p0.toString()) {
                    getString(R.string.netflix) -> PlatformEnum.NETFLIX
                    getString(R.string.apple) -> PlatformEnum.APPLE
                    getString(R.string.disney) -> PlatformEnum.DISNEY
                    getString(R.string.hbo) -> PlatformEnum.HBO
                    getString(R.string.paramount) -> PlatformEnum.PARAMOUNT
                    getString(R.string.prime) -> PlatformEnum.PRIME_VIDEO
                    getString(R.string.star) -> PlatformEnum.STAR
                    else -> PlatformEnum.OTHER
                }
                changeHeaderImage(platform)
            }

        })
    }

    private fun validateFields(): Boolean {
        var valid = true
        if(binding.tietTitle.text.toString().isEmpty())
        {
            binding.tilTitle.error = getString(R.string.title_error)
            valid = false
        }
        if(binding.tietGenre.text.toString().isEmpty())
        {
            binding.tilGenre.error = getString(R.string.genre_error)
            valid = false
        }
        if(binding.actvPlatform.text.toString().isEmpty())
        {
            binding.tilPlatform.error = getString(R.string.platform)
            valid = false
        }

        return valid
    }

    private fun buildDialog(
        positiveBtn: String,
        negativeBtn: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.alertTitle))
            .setPositiveButton(positiveBtn) { dialog, which ->
                positiveAction()
            }
            .setNegativeButton(negativeBtn) { dialog, which ->
                negativeAction()
            }
            .create()

    private fun changeHeaderImage(platformEnum: PlatformEnum) {
        movieEntity.platformImage = platformEnum

        Glide.with(binding.ivHeader.context)
            .load(movieEntity.platformImage.id)
            .error(PlatformEnum.OTHER.id)
            .into(binding.ivHeader)
    }
}