package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.CheckInAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentCheckInBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.util.Calendar

class CheckInFragment : Fragment() {

    private val binding : FragmentCheckInBinding by lazy {
        FragmentCheckInBinding.inflate(layoutInflater)
    }

    val todaysArraylist : ArrayList<UserQuestionModel> = ArrayList()
    val weeksArraylist : ArrayList<UserQuestionModel> = ArrayList()
    val monthsArraylist : ArrayList<UserQuestionModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {





        setListener()
        setView()

        return binding.root
    }

    private fun setView() {
        binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
        binding.weekTV.setBackgroundResource(0)
        binding.monthTV.setBackgroundResource(0)
        todaysArraylist.clear()
        todaysCheckins()

    }

    private fun setListener() {
        binding.todayTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(R.drawable.check_in_bg_today)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(0)
            todaysArraylist.clear()
            todaysCheckins()
        }
        binding.weekTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(R.drawable.check_in_bg_week)
            binding.monthTV.setBackgroundResource(0)
            weeksArraylist.clear()
            weeksCehckins()
        }
        binding.monthTV.setOnClickListener {
            binding.todayTV.setBackgroundResource(0)
            binding.weekTV.setBackgroundResource(0)
            binding.monthTV.setBackgroundResource(R.drawable.check_in_bg_month)
            monthsArraylist.clear()
            monthsCheckins()
        }
    }

    private fun monthsCheckins() {
        Constants.currentUserQuestion.forEach { it ->
            if (isSameMonth(System.currentTimeMillis(),it.date.toLong()))
            {
                monthsArraylist.add(it)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.recyclerView.adapter = CheckInAdapter(requireActivity(), monthsArraylist)

            }

        }
    }

    private fun weeksCehckins()
    {
        Constants.currentUserQuestion.forEach { it ->
            if (isSameWeek(System.currentTimeMillis(),it.date.toLong()))
            {
                weeksArraylist.add(it)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.recyclerView.adapter = CheckInAdapter(requireActivity(), weeksArraylist)
            }
        }

    }

    private fun todaysCheckins(){

        Constants.currentUserQuestion.forEach { it ->
            if (isSameDay(System.currentTimeMillis(),it.date.toLong()))
            {
                todaysArraylist.add(it)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                binding.recyclerView.adapter = CheckInAdapter(requireActivity(), todaysArraylist)
            }
        }

    }


    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun isSameWeek(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR)
    }

    fun isSameMonth(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
    }
}