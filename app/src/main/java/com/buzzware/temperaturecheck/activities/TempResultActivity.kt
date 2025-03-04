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
import com.buzzware.temperaturecheck.adapters.ViewPagerAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityTempResultBinding
import com.buzzware.temperaturecheck.fragments.MonthGraphFragment
import com.buzzware.temperaturecheck.fragments.TodaysGraphFragment
import com.buzzware.temperaturecheck.fragments.WeekGraphFragment
import com.google.api.Context

class TempResultActivity : BaseActivity() {

    private val binding : ActivityTempResultBinding by lazy {
        ActivityTempResultBinding.inflate(layoutInflater)
    }

    private var res : Int = 0
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        res = intent.getIntExtra("ResultOfMode",0)

        setView()
        setListener()

    }

    private fun setView() {


        binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
        val fragment = listOf(TodaysGraphFragment(res))
        viewPagerAdapter = ViewPagerAdapter(fragment,this)
        binding.viewPager.adapter = viewPagerAdapter

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.todayTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(0)
            viewPagerAdapter.updateFragments(listOf(TodaysGraphFragment(res)))

        }
        binding.weekTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(R.drawable.check_in_bg_week)
            binding.monthTV.setBackgroundResource(0)
            viewPagerAdapter.updateFragments(listOf(WeekGraphFragment()))


        }
        binding.monthTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(R.drawable.check_in_bg_month)
            viewPagerAdapter.updateFragments(listOf(MonthGraphFragment()))


        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}