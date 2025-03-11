package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentWeekGraphBinding
import com.buzzware.temperaturecheck.model.ChatResponse
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.buzzware.temperaturecheck.retrofit.AIController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MonthGraphFragment(
    val res: Int,
    val selectedUser: UserModel,
    val selectedQuestions: ArrayList<UserQuestionModel>
) : BaseFragment() {

    private val binding: FragmentWeekGraphBinding by lazy {
        FragmentWeekGraphBinding.inflate(layoutInflater)
    }

    private var total : Int = 1
    private var color : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        color = when(res){
            1 ->{"red"}
            2 ->{"light red"}
            3 ->{"yellow"}
            4 ->{"light green"}
            5 ->{"green"}
            else -> { "red"}
        }

        val monthRange = getCurrentMonthRange()
        val monthStart = monthRange.first
        val monthEnd = monthRange.second
        val circleColors = ArrayList<Int>()

        Log.d("LOGGER", "Month Start: ${Date(monthStart)} - Month End: ${Date(monthEnd)}")

        val filteredMonthData = selectedQuestions.filter {
            it.date in monthStart..monthEnd
        }

        filteredMonthData.forEach {
            Log.d("LOGGER", "Model: ${it.date}, Answer: ${it.answer}")
        }

        val monthDates = getCurrentMonthDates()
        Log.d("LOGGER", "Month Days: $monthDates")

        val moodAverages = filteredMonthData.groupBy { getDayFromTimestamp(it.date.toLong()) }
            .mapValues { (_, questions) ->
                val moodScores = questions.mapNotNull { getMoodScore(it.answer) }
                if (moodScores.isNotEmpty()) moodScores.average().toFloat() else 0f
            }

        Log.d("LOGGER", "Mood Averages: $moodAverages")

        val entries = mutableListOf<Entry>()

        monthDates.forEachIndexed { index, date ->
            val avgMood = moodAverages[date] ?: 0f
            entries.add(Entry(index.toFloat(), avgMood))
            Log.d("LOGGER", "Graph Entry -> Date: $date, Mood Avg: $avgMood")

            val color = when (avgMood) {
                in 0.1..1.0 -> ContextCompat.getColor(requireContext(), R.color.angry)
                in 1.1..2.0 -> ContextCompat.getColor(requireContext(), R.color.sad)
                in 2.1..3.0 -> ContextCompat.getColor(requireContext(), R.color.uneasy)
                in 3.1..4.0 -> ContextCompat.getColor(requireContext(), R.color.happy)
                in 4.1..5.0 -> ContextCompat.getColor(requireContext(), R.color.very_happy)
                else -> ContextCompat.getColor(requireContext(), R.color.transparent) // Default color
            }
            circleColors.add(color)
        }

        val dataSets = mutableListOf<LineDataSet>()
        var previousValidEntry: Entry? = null

        for (entry in entries) {
            if (entry.y != 0f) { // Ignore 0.0 values
                if (previousValidEntry != null) {
                    val segmentDataSet = LineDataSet(listOf(previousValidEntry, entry), "Mood Segment").apply {
                        color = getMoodColor(previousValidEntry!!.y)
                        setDrawCircles(false) // No circles on segment lines
                        lineWidth = 4f
                        setDrawValues(false)
                    }
                    dataSets.add(segmentDataSet)
                }
                previousValidEntry = entry
            }
        }

        val pointDataSet = LineDataSet(entries, "Mood Points").apply {
            setDrawCircles(true)
            setDrawValues(false)
            color = ContextCompat.getColor(requireActivity(), R.color.transparent)
            lineWidth = 0f
            setDrawCircles(true)
            circleRadius = 7f
            setDrawCircleHole(false)
            this.circleColors = circleColors
            setDrawFilled(true)
        }

        dataSets.add(pointDataSet)

        binding.lineChart.data = LineData(dataSets as List<ILineDataSet>)

        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // Only show dates at intervals of 3 (01, 03, 06, ...)
        xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index % 3 == 0) monthDates.getOrNull(index) ?: "" else ""
            }
        }

        binding.lineChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 5f
            granularity = 1f
            setDrawGridLines(false)
            setDrawLabels(false)
        }
        binding.lineChart.axisRight.isEnabled = false

        binding.lineChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            invalidate()
            animateXY(2000, 2000, Easing.EaseInOutQuad)
        }

        binding.lineChart.invalidate()

        setViews()
        hitApi()

        return binding.root
    }

    private fun setViews() {
        binding.nameTV.text = "${selectedUser.firstName} ${selectedUser.lastName}"
        Glide.with(this)
            .load(selectedUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.rootIV)


        if(res == 5) {
            color = "green"
            binding.personIV5.visibility = View.VISIBLE
            Glide.with(this)
                .load(selectedUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV5)
        }else if(res == 4) {
            color = "light green"
            binding.personIV4.visibility = View.VISIBLE
            Glide.with(this)
                .load(selectedUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV4)

        }else if(res == 3) {
            color = "yellow"
            binding.personIV3.visibility = View.VISIBLE
            Glide.with(this)
                .load(selectedUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV3)

        }else if(res == 2) {
            color = "light red"
            binding.personIV2.visibility = View.VISIBLE
            Glide.with(this)
                .load(selectedUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV2)

        }else if(res == 1) {
            color = "red"
            binding.personIV1.visibility = View.VISIBLE
            Glide.with(this)
                .load(selectedUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV1)

        }
    }

    private fun hitApi() {
        mDialog.show()
        Log.d("LOGGER", "Total: $total, Color: $color")
        val query = "We are $total group members and our average current temperature level is $color. " +
                "So [red is angry, light red is sad, yellow is uneasy, light green is happy, green is very happy]. " +
                "What can we do today according to this color?"

        val requestBody = mapOf(
            "model" to "gpt-3.5-turbo-0125",
            "messages" to listOf(mapOf("role" to "user", "content" to query)),
            "temperature" to 0,
            "max_tokens" to 1000,
            "top_p" to 1,
            "frequency_penalty" to 0.0,
            "presence_penalty" to 0.0
        )

        AIController.instance.getChatResponse(requestBody)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    mDialog.dismiss()
                    if (response.isSuccessful) {
                        binding.contentTV.text = response.body()?.choices?.firstOrNull()?.message?.content.toString()
                    }else{
                        showAlert("Message: $response")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    mDialog.dismiss()
                    showAlert("Message: ${t.message}")
                }
            })
    }

    private fun getMoodScore(answer: String?): Float? {
        return when (answer) {
            "Very Happy" -> 5f
            "Happy" -> 4f
            "Uneasy" -> 3f
            "Sad" -> 2f
            "Angry" -> 1f
            else -> null
        }
    }

    fun getCurrentMonthDates(): List<String> {
        val monthRange = getCurrentMonthRange()
        val sdfDay = SimpleDateFormat("dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = monthRange.first

        val dateList = mutableListOf<String>()

        while (calendar.timeInMillis <= monthRange.second) {
            dateList.add(sdfDay.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dateList
    }

    fun getCurrentMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 0) // Last day of previous month
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    fun getDayFromTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun getMoodColor(moodScore: Float): Int {
        return when (moodScore) {
            in 0.1..1.0 -> ContextCompat.getColor(requireContext(), R.color.angry)
            in 1.1..2.0 -> ContextCompat.getColor(requireContext(), R.color.sad)
            in 2.1..3.0 -> ContextCompat.getColor(requireContext(), R.color.uneasy)
            in 3.1..4.0 -> ContextCompat.getColor(requireContext(), R.color.happy)
            in 4.1..5.0 -> ContextCompat.getColor(requireContext(), R.color.very_happy)
            else -> ContextCompat.getColor(requireContext(), R.color.transparent)
        }
    }

    fun getTextToShare(): String {
        return binding.contentTV.text.toString()
    }
}
