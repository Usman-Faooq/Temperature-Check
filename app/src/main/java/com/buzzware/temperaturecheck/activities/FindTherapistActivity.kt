package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ActivityFindTherapistBinding

class FindTherapistActivity : BaseActivity() {

    private val binding : ActivityFindTherapistBinding by lazy {
        ActivityFindTherapistBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setView()
        setListener()

    }

    private fun setView() {

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.icon1.setOnClickListener { goToNextActivity() }
        binding.icon2.setOnClickListener { goToNextActivity() }
        binding.icon3.setOnClickListener { goToNextActivity() }
        binding.icon4.setOnClickListener { goToNextActivity() }

    }

    private fun goToNextActivity() {
        startActivity(Intent(this, TherapistDetailActivity::class.java))
        overridePendingTransition(fadeIn, fadeOut)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

}