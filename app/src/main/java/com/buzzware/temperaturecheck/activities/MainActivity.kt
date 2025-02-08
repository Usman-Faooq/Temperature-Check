package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()

    }

    private fun setListener() {

        binding.signInTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.signUpTV.setOnClickListener {
            startActivity(Intent(this, NewSelectorActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

    }
}