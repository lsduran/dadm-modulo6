package com.dualser.modulo6.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dualser.modulo6.R
import com.dualser.modulo6.databinding.ActivityAuthBinding
import com.dualser.modulo6.ui.fragments.LoginFragment
import com.dualser.modulo6.ui.fragments.MoviesListFragment

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, LoginFragment())
                .commit()
        }
    }
}