package com.buzzware.temperaturecheck.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ActivityTherapistDetailBinding

class TherapistDetailActivity : BaseActivity() {

    private val binding : ActivityTherapistDetailBinding by lazy {
        ActivityTherapistDetailBinding.inflate(layoutInflater)
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



    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

}