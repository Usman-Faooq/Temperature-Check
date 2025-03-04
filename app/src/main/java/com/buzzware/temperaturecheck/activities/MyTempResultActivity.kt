package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityMyTempResultBinding

class MyTempResultActivity : BaseActivity() {

    private val binding : ActivityMyTempResultBinding by lazy {
        ActivityMyTempResultBinding.inflate(layoutInflater)
    }
    private var user_mode : Int? = 0
    private var total : Int = 0
    private var res : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user_mode = intent.getIntExtra("User_Mode",0)
        total = intent.getIntExtra("total",0)

        setView()
        setListener()

    }

    private fun setView() {
        res = user_mode?.div(total)

        if(res == 1) {
            binding.personIV5.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV5)
        }else if(res == 2) {
            binding.personIV4.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV4)

        }else if(res == 3) {
            binding.personIV3.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV3)

        }else if(res == 4) {
            binding.personIV2.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV2)

        }else if(res == 5) {
            binding.personIV1.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV1)

        }

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.findTV.setOnClickListener {
            startActivity(Intent(this, FindTherapistActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.DetailedAnalytics.setOnClickListener {
            val intent = Intent(this,TempResultActivity::class.java)
            intent.putExtra("ResultOfMode",res)
            startActivity(intent)
            overridePendingTransition(fadeIn, fadeOut)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,IndividualHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(fadeIn, fadeOut)
    }
}