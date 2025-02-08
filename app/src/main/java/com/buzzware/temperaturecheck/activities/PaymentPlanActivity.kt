package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityPaymentPlanBinding

class PaymentPlanActivity : BaseActivity() {

    private val binding : ActivityPaymentPlanBinding by lazy {
        ActivityPaymentPlanBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)

        setListener()

    }


    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.monthlyLayout.setOnClickListener {
            binding.monthlyRadio.setImageResource(R.drawable.ic_radio_selected)
            binding.annualRadio.setImageResource(R.drawable.ic_radio_non)
        }

        binding.annualLayout.setOnClickListener {
            binding.annualRadio.setImageResource(R.drawable.ic_radio_selected)
            binding.monthlyRadio.setImageResource(R.drawable.ic_radio_non)
        }

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

}