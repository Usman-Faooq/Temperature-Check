package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.DaysInRowItemDesignBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DaysInRowAdapter(
    val requireActivity: Context,
    val arraylistDays: ArrayList<String>,
    val arraylistItems: ArrayList<UserQuestionModel>,
    val listener: ItemClicked
) : RecyclerView.Adapter<DaysInRowAdapter.ViewHolder>() {


    interface ItemClicked
    {
        fun onItemClicked(dayIcon: CharSequence)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DaysInRowItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {
        return arraylistDays.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayName = arraylistDays[position]
        val currentWeekRange = getCurrentWeekRange()

        var foundMatch = false
        var check = ""

        arraylistItems.forEach { item ->
            val itemTimestamp = item.date.toLong()

            if (itemTimestamp in currentWeekRange.first..currentWeekRange.second) {
                val itemDayName = getDayName(itemTimestamp)

                if (itemDayName == dayName) {
                    //holder.binding.dayIcon.setImageResource(R.drawable.tick)
                    foundMatch = true
                    check = "found"
                }
            }
        }

        if (!foundMatch) {
            if (isFutureDay(dayName)){
                check = "future"
                holder.binding.dayIcon.setImageResource(R.drawable.questionmark)
            }else{
                check = "past"
                holder.binding.dayIcon.setImageResource(R.drawable.add)
            }

        }else{
            holder.binding.dayIcon.setImageResource(R.drawable.tick)
        }


        holder.binding.daysName.text = arraylistDays[position]
        holder.binding.dayIcon.setOnClickListener {
            if (check == "past"){
                listener.onItemClicked(getDateFromDay(getCurrentWeekRange(),holder.binding.daysName.text.toString()).toString())
            }
        }

    }

    fun getDayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEE", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
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
    fun getDateFromDay(weekRange: Pair<Long, Long>, dayName: String): String? {
        val sdfEEE = SimpleDateFormat("EEE", Locale.getDefault()) // Formatter for "Mon", "Tue", etc.
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formatter for "yyyy-MM-dd"

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = weekRange.first // Start from Monday

        for (i in 0..6) { // Iterate through the 7 days of the week
            if (sdfEEE.format(calendar.time) == dayName) {
                return sdfDate.format(calendar.time) // Return date in "yyyy-MM-dd" format
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Move to next day
        }
        return null // If the day name is invalid
    }


    fun isFutureDay(dayName: String): Boolean {
        val calendar = Calendar.getInstance()
        val currentDayName = SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)

        val weekDays = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        return weekDays.indexOf(dayName) > weekDays.indexOf(currentDayName)
    }

    inner class ViewHolder(val binding : DaysInRowItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}


