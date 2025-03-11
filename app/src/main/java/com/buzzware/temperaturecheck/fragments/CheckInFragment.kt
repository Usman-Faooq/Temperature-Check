package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import android.util.Log
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
    val updateArraylist : ArrayList<UserQuestionModel> = ArrayList()

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

        updateArraylist.clear()

        val morningScores = mutableListOf<Int>()
        val afternoonScores = mutableListOf<Int>()
        val eveningScores = mutableListOf<Int>()


        Constants.currentUserQuestion.forEach { it ->
            if (isSameMonth(System.currentTimeMillis(),it.date.toLong()))
            {
                monthsArraylist.add(it)


                when (it.today) {
                    "Morning" -> morningScores.add(getMoodScore(it.answer))
                    "Afternoon" -> afternoonScores.add(getMoodScore(it.answer))
                    "Evening" -> eveningScores.add(getMoodScore(it.answer))
                }

            }
        }

        val morningAvg = if (morningScores.isNotEmpty()) morningScores.sum() / morningScores.size else 0
        val afternoonAvg = if (afternoonScores.isNotEmpty()) afternoonScores.sum() / afternoonScores.size else 0
        val eveningAvg = if (eveningScores.isNotEmpty()) eveningScores.sum() / eveningScores.size else 0


        if (morningAvg > 0){
            val models = monthsArraylist.filter { it.today == "Morning" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }

        }

        if (afternoonAvg > 0){

            val models = monthsArraylist.filter { it.today == "Afternoon" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }
        }

        if (eveningAvg > 0){
            val models = monthsArraylist.filter { it.today == "Evening" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }
        }
        updateArraylist.sortedByDescending {
            it.date1
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = CheckInAdapter(requireActivity(), updateArraylist)


    }

    private fun weeksCehckins()
    {
        updateArraylist.clear()

        val morningScores = mutableListOf<Int>()
        val afternoonScores = mutableListOf<Int>()
        val eveningScores = mutableListOf<Int>()



        Constants.currentUserQuestion.forEach { it ->
            if (isSameWeek(System.currentTimeMillis(),it.date.toLong()))
            {
                weeksArraylist.add(it)


                when (it.today) {
                    "Morning" -> morningScores.add(getMoodScore(it.answer))
                    "Afternoon" -> afternoonScores.add(getMoodScore(it.answer))
                    "Evening" -> eveningScores.add(getMoodScore(it.answer))
                }

            }
        }

        Log.d("LOGGER","Size of weekList ${weeksArraylist.size}")



        val morningAvg = if (morningScores.isNotEmpty()) morningScores.sum() / morningScores.size else 0
        val afternoonAvg = if (afternoonScores.isNotEmpty()) afternoonScores.sum() / afternoonScores.size else 0
        val eveningAvg = if (eveningScores.isNotEmpty()) eveningScores.sum() / eveningScores.size else 0


        if (morningAvg > 0){
            val models = weeksArraylist.filter { it.today == "Morning" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }

        }

        if (afternoonAvg > 0){
            val models = weeksArraylist.filter { it.today == "Afternoon" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }
        }

        if (eveningAvg > 0){

            val models = weeksArraylist.filter { it.today == "Evening" }

            models.forEach { model ->
                model.avg = morningAvg.toDouble()
                updateArraylist.add(model)
            }
        }

        Log.d("LOGGER","updateArraylist size${updateArraylist.size}")

        updateArraylist.sortedByDescending {
            it.date1
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = CheckInAdapter(requireActivity(), updateArraylist)


    }

    private fun todaysCheckins(){

        updateArraylist.clear()

        val morningScores = mutableListOf<Int>()
        val afternoonScores = mutableListOf<Int>()
        val eveningScores = mutableListOf<Int>()

        Constants.currentUserQuestion.forEach { it ->
            if (isSameDay(System.currentTimeMillis(),it.date.toLong()))
            {
                todaysArraylist.add(it)


                when (it.today) {
                    "Morning" -> morningScores.add(getMoodScore(it.answer))
                    "Afternoon" -> afternoonScores.add(getMoodScore(it.answer))
                    "Evening" -> eveningScores.add(getMoodScore(it.answer))
                }


            }
        }

        // Calculate averages
        val morningAvg = if (morningScores.isNotEmpty()) morningScores.sum() / morningScores.size else 0
        val afternoonAvg = if (afternoonScores.isNotEmpty()) afternoonScores.sum() / afternoonScores.size else 0
        val eveningAvg = if (eveningScores.isNotEmpty()) eveningScores.sum() / eveningScores.size else 0


        if (morningAvg > 0){
            val model = todaysArraylist.firstOrNull { it.today == "Morning" }
            model!!.avg = morningAvg.toDouble()
            updateArraylist.add(model)
        }

        if (afternoonAvg > 0){
            val model = todaysArraylist.firstOrNull { it.today == "Afternoon" }
            model!!.avg = afternoonAvg.toDouble()
            updateArraylist.add(model)
        }

        if (eveningAvg > 0){
            val model = todaysArraylist.firstOrNull { it.today == "Evening" }
            model!!.avg = eveningAvg.toDouble()
            updateArraylist.add(model)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = CheckInAdapter(requireActivity(), updateArraylist)


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


    private fun getMoodScore(answer: String?): Int {
        return when (answer) {
            "Very Happy" -> 5
            "Happy" -> 4
            "Uneasy" -> 3
            "Sad" -> 2
            "Angry" -> 1
            else -> 0
        }
    }
}