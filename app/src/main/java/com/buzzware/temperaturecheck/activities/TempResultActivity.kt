package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ActivityTempResultBinding

class TempResultActivity : BaseActivity() {

    private val binding : ActivityTempResultBinding by lazy {
        ActivityTempResultBinding.inflate(layoutInflater)
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

        binding.findTV.setOnClickListener {
            startActivity(Intent(this, FindTherapistActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}