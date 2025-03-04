package com.buzzware.temperaturecheck.fragments

import CustomYAxisRenderer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentWeekGraphBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeekGraphFragment : Fragment() {

    private val binding: FragmentWeekGraphBinding by lazy {
        FragmentWeekGraphBinding.inflate(layoutInflater)
    }
    val weekListDates = ArrayList<Float>()
    val weekList = ArrayList<UserQuestionModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        Constants.currentUserQuestion.forEach {
            if (isSameWeek(System.currentTimeMillis(), it.date.toLong())) {
                weekListDates.add(getDayFromTimestamp(it.date.toLong()).toFloat())
                weekList.add(it)
            }
        }


        getAverageOfPrevoius()

        return binding.root
    }

    private fun getAverageOfPrevoius() {
        val totalList = ArrayList<Float>()
        val totalAvgList = ArrayList<Float>()
        var totalAvg: Float = 0.0f

        val groupedByDate = weekList.groupBy { it.date1 }

        groupedByDate.forEach { (date, questions) ->

            questions.forEach {
                when (it.answer) {
                    "Very Happy" -> totalList.add(1f)
                    "Happy" -> totalList.add(2f)
                    "Uneasy" -> totalList.add(3f)
                    "Sad" -> totalList.add(4f)
                    "Angry" -> totalList.add(5f)
                }
            }

            totalList.forEach {
                totalAvg += it
            }


            totalAvgList.add(totalAvg / totalList.size)
            totalList.clear()
            totalAvg = 0f
        }

        setupLineChart(weekListDates.distinct().sortedDescending(),totalAvgList)
    }


    private fun setupLineChart(weekDayDates: List<Float>, totalAvgList: ArrayList<Float>) {
        val entries = ArrayList<Entry>()
       val circleColors = ArrayList<Int>()

//        entries.add(Entry(11f,2.3f))
//        entries.add(Entry(12f,3.3f))
//        entries.add(Entry(13f,4.3f))
//        totalAvgList.forEachIndexed { index, fl ->
//            entries.add(Entry(index.toFloat(),fl))
//        }
        //entries.add(Entry(25f,2.3f))
        for(i in totalAvgList.indices){
            entries.add(Entry(weekDayDates[i].toFloat(), totalAvgList[i])) // Adding data to entries

            val color = when {
                totalAvgList[i] in 1.0..2.0 -> ContextCompat.getColor(requireContext(), R.color.very_happy)
                totalAvgList[i] in 2.1..3.0 -> ContextCompat.getColor(requireContext(), R.color.happy)
                totalAvgList[i] in 3.1..4.0 -> ContextCompat.getColor(requireContext(), R.color.uneasy)
                totalAvgList[i] in 4.1..5.0 -> ContextCompat.getColor(requireContext(), R.color.sad)
                else -> ContextCompat.getColor(requireContext(), R.color.angry) // Default color
            }
            circleColors.add(color)

        }

        Log.d("TAG","${entries}")


        val dataSet = LineDataSet(entries, "").apply {
            lineWidth = 4f
            setDrawCircles(true) // Ensure circles are drawn
            setCircleColors(circleColors) // Apply dynamic colors
            circleRadius = 10f
            setDrawCircleHole(false) // Make circles solid (no hole inside)

            // Enable gradient fill
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chartbg)
        }

        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData

        binding.lineChart.apply {

            //removeBGLines
            setDrawGridBackground(false)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)

            binding.lineChart.axisRight.isEnabled = false  // Hide right Y-axis

            binding.lineChart.axisLeft.apply {
                isGranularityEnabled = true
                granularity = 1f  // Ensure integer steps
                axisMinimum = 0f  // Set a fixed minimum Y value (to prevent hiding)
                axisMaximum = 5f  // Max Y value + 1 for better spacing
            }


            binding.lineChart.xAxis.apply {
                granularity = 1f  // Ensure values step by 1 (no decimals)
                isGranularityEnabled = true  // Enforce granularity
                position = XAxis.XAxisPosition.BOTTOM
            }


            animateXY(2000, 2000, Easing.EaseInOutQuad)


            invalidate()


        }


    }


        fun isSameWeek(timestamp1: Long, timestamp2: Long): Boolean {
            val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
            val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

            return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR)
        }


    fun getCurrentWeekDates(): List<String> {
        val weekRange = getCurrentWeekRange() // Get start and end of the current week
        val sdfDay = SimpleDateFormat("dd", Locale.getDefault()) // Format to extract day (dd)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = weekRange.first // Start from Monday

        val dateList = mutableListOf<String>()

        while (calendar.timeInMillis <= weekRange.second) { // Loop through the range
            dateList.add(sdfDay.format(calendar.time)) // Add "dd" format date
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to next day
        }

        return dateList // Return list of dates
    }

    fun getCurrentWeekRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.timeInMillis


        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfWeek = calendar.timeInMillis

        return Pair(startOfWeek, endOfWeek)
    }

        fun getDayFromTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
