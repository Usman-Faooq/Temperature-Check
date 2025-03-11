package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ActivityTempResultBinding
import com.buzzware.temperaturecheck.fragments.MonthGraphFragment
import com.buzzware.temperaturecheck.fragments.TodaysGraphFragment
import com.buzzware.temperaturecheck.fragments.WeekGraphFragment
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel

class TempResultActivity : BaseActivity() {

    private val binding : ActivityTempResultBinding by lazy {
        ActivityTempResultBinding.inflate(layoutInflater)
    }

    private var selectedUser: UserModel = UserModel()
    private var selectedQuestions: ArrayList<UserQuestionModel> = arrayListOf()

    private var res : Int = 0

    private var tabType = "today"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectedUser = intent.getParcelableExtra("userModel")?: UserModel()
        selectedQuestions = intent.getParcelableArrayListExtra("questions_list")?: arrayListOf()
        res = intent.getIntExtra("ResultOfMode",0)

        Log.d("LOGGER", "selecteQestion: ${selectedQuestions.size}")

        setView()
        setListener()

    }

    private fun setView() {

        binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
        loadFragment(TodaysGraphFragment(res, selectedUser, selectedQuestions))

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.todayTV.setOnClickListener {
            tabType = "day"
            binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(0)

            loadFragment(TodaysGraphFragment(res, selectedUser, selectedQuestions))
        }

        binding.weekTV.setOnClickListener {
            tabType = "week"
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(R.drawable.check_in_bg_week)
            binding.monthTV.setBackgroundResource(0)

            loadFragment(WeekGraphFragment(res, selectedUser, selectedQuestions))
        }

        binding.monthTV.setOnClickListener {
            tabType = "month"
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(R.drawable.check_in_bg_month)

            loadFragment(MonthGraphFragment(res, selectedUser, selectedQuestions))
        }

        binding.shareIV.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(binding.container.id)
            val shareText = when(currentFragment){
                is TodaysGraphFragment -> currentFragment.getTextToShare()
                is WeekGraphFragment -> currentFragment.getTextToShare()
                is MonthGraphFragment -> currentFragment.getTextToShare()
                else -> "No Content Available."
            }

            shareText(shareText)
        }
    }

    private fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fadeIn, fadeOut)
        transaction.replace(binding.container.id, fragment)
        transaction.commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}