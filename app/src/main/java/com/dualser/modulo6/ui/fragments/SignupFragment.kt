package com.dualser.modulo6.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dualser.modulo6.R
import com.dualser.modulo6.databinding.FragmentSignupBinding
import com.dualser.modulo6.ui.activities.MainActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null

    private lateinit var firebaseAuth: FirebaseAuth

    private var name = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {

            if (!validate()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            signup()
        }
    }

    private fun validate(): Boolean {
        name = binding.tietName.text.toString().trim()
        email = binding.tietEmail.text.toString().trim()
        password = binding.tietPassword.text.toString().trim()
        confirmPassword = binding.tietConfirmPassword.text.toString().trim()

        if (name.isEmpty()){
            binding.tietName.error = getString(R.string.name_required)
            binding.tietName.requestFocus()
            return false
        }

        if (email.isEmpty()){
            binding.tietEmail.error = getString(R.string.email_required)
            binding.tietEmail.requestFocus()
            return false
        }

        if (password.isEmpty()){
            binding.tietPassword.error = getString(R.string.password_required)
            binding.tietPassword.requestFocus()
            return false
        }

        if (password.compareTo(confirmPassword) != 0){
            binding.tietConfirmPassword.error = getString(R.string.password_no_match)
            binding.tietConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun signup() {
        //Registrando al usuario
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult->
            if(authResult.isSuccessful){
                //Enviar correo para verificación de email
                var firebaseUser = firebaseAuth.currentUser
                firebaseUser?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(requireContext(), getString(R.string.verification_email_sent), Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(requireContext(), getString(R.string.verification_email_error), Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(requireContext(), getString(R.string.user_created), Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                binding.progressBar.visibility = View.GONE
                signUpErrors(authResult)
            }
        }
    }

    private fun signUpErrors(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                binding.tietEmail.error = getString(R.string.error_invalid_email)
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                binding.tietPassword.error = getString(R.string.error_wrong_password)
                binding.tietPassword.requestFocus()
                binding.tietPassword.setText("")
            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                Toast.makeText(requireContext(), getString(R.string.error_account_exists_with_different_credential), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                binding.tietEmail.error = getString(R.string.error_email_already_in_use)
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(requireContext(), getString(R.string.error_user_token_expired), Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(requireContext(), getString(R.string.error_user_not_found), Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                binding.tietPassword.error = getString(R.string.error_weak_password)
                binding.tietPassword.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(requireContext(), getString(R.string.no_network), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), getString(R.string.authentication_error), Toast.LENGTH_SHORT).show()
            }
        }

    }

}