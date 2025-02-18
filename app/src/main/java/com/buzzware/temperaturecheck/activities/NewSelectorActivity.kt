package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityNewSelectorBinding

class NewSelectorActivity : BaseActivity() {

    private val binding : ActivityNewSelectorBinding by lazy {
        ActivityNewSelectorBinding.inflate(layoutInflater)
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

        binding.individualTV.setOnClickListener {
            Constants.selectedType = "individual"
            setSelector(binding.individualTV, binding.familyTV, binding.teamTV, binding.classroomTV)
        }

        binding.familyTV.setOnClickListener {
            Constants.selectedType = "family"
            setSelector(binding.familyTV, binding.individualTV, binding.teamTV, binding.classroomTV)
        }

        binding.teamTV.setOnClickListener {
            Constants.selectedType = "team"
            setSelector(binding.teamTV, binding.familyTV, binding.individualTV, binding.classroomTV)
        }

        binding.classroomTV.setOnClickListener {
            Constants.selectedType = "classroom"
            setSelector(binding.classroomTV, binding.familyTV, binding.teamTV, binding.individualTV)
        }

        binding.nextBtn.setOnClickListener {
            if (Constants.selectedType != ""){

                startActivity(Intent(this, SignUpActivity::class.java))
                overridePendingTransition(fadeIn, fadeOut)

            }else{
                showAlert("Please Select Option")
            }
        }

    }

    private fun setSelector(selected: TextView, t0: TextView, t1: TextView, t2: TextView) {

        selected.setBackgroundResource(R.drawable.rounded_selector)
        t0.setBackgroundResource(R.drawable.rounded_with_gray_border)
        t1.setBackgroundResource(R.drawable.rounded_with_gray_border)
        t2.setBackgroundResource(R.drawable.rounded_with_gray_border)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}