package com.buzzware.temperaturecheck.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.QuestionAdapter
import com.buzzware.temperaturecheck.databinding.ActivityStartCheckInBinding

class StartCheckInActivity : BaseActivity() {

    private val binding : ActivityStartCheckInBinding by lazy {
        ActivityStartCheckInBinding.inflate(layoutInflater)
    }

    private val questionList = arrayListOf("How did you sleep?", "How do you feel about Yourself today?", "Are you looking forward to today?")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)

        setView()
        setListener()

    }

    private fun setView() {

        binding.questionVP.isUserInputEnabled = false
        binding.questionVP.adapter = QuestionAdapter(this, questionList)
        updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.nextLayout.setOnClickListener {
            if (binding.questionVP.currentItem < questionList.size - 1 && binding.nextTV.text.toString() != "Complete") {
                binding.questionVP.currentItem += 1
                updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)
                if (binding.questionVP.currentItem == questionList.size - 1){
                    binding.nextTV.text = "Complete"
                }else{
                    binding.nextTV.text = "Next"
                }
            } else {
                //complete click
                startActivity(Intent(this, MyTempResultActivity::class.java))
                overridePendingTransition(fadeIn, fadeOut)
            }
        }

        binding.backLayout.setOnClickListener {
            val currentItem = binding.questionVP.currentItem
            if (currentItem > 0) {
                binding.questionVP.currentItem = currentItem - 1
                updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

                if (binding.questionVP.currentItem < questionList.size - 1) {
                    binding.nextTV.text = "Next"
                }
            } else {
                // User is already on the first question
                showToast("You are already on the first question.")
            }
        }

    }

    private fun updateCurrentPosition(current: Int, total: Int) {
        binding.currentPositionTV.text = current.toString()
        binding.totalQuestionTV.text = " / $total"
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}