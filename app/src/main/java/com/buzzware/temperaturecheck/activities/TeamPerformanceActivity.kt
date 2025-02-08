package com.buzzware.temperaturecheck.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.AddTeamAdapter
import com.buzzware.temperaturecheck.adapters.TeamAdapter
import com.buzzware.temperaturecheck.databinding.ActivityTeamPerformanceBinding

class TeamPerformanceActivity : BaseActivity() {

    private val binding : ActivityTeamPerformanceBinding by lazy {
        ActivityTeamPerformanceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setView()
        setListener()

    }

    private fun setView() {

        binding.recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView2.adapter = TeamAdapter(this, arrayListOf())


    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}