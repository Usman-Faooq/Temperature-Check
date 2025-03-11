package com.buzzware.temperaturecheck.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.MyTempResultActivity
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ItemDesignCheckInLayoutBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.util.ArrayList

class CheckInAdapter(val context: Context, val questionList: ArrayList<UserQuestionModel>) : RecyclerView.Adapter<CheckInAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignCheckInLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = questionList[position]

        holder.binding.dateTV.text = list.date1


        val layerDrawable = holder.binding.progressSlider.progressDrawable as LayerDrawable
        val clipDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        val progressDrawable = clipDrawable.drawable as GradientDrawable

        holder.binding.periodOfDay.text = list.today

        val totalprogress = when(list.avg){
            5.0 -> {100}
            4.0 -> {80}
            3.0 -> {60}
            2.0 -> {40}
            1.0 -> {20}
            else -> {0}
        }

        holder.binding.progressSlider.progress = totalprogress.toInt()

        var userMode = 0
        when(list.answer)
        {
            "Very Happy" -> {
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.very_happy))
                holder.binding.progressSlider.progress = 100
                userMode += 5
            }
            "Happy" -> {
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.happy))
                holder.binding.progressSlider.progress = 80
                userMode += 4
            }
            "Uneasy" -> {
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.uneasy))
                holder.binding.progressSlider.progress = 60
                userMode += 3

            }
            "Sad" -> {
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.sad))
                holder.binding.progressSlider.progress = 40
                userMode += 2
            }
            "Angry" -> {
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.angry))
                holder.binding.progressSlider.progress = 20
                userMode += 1
            }
        }

       // holder.binding.progressSlider.setOnTouchListener { _, _ -> true }

        holder.binding.root.setOnClickListener {
            Log.d("LOGGER", "adapter Mode: $userMode")
            Log.d("LOGGER", "adapter total: ${questionList.size}")
            context.startActivity(Intent(context, MyTempResultActivity::class.java)
                .putExtra("userModel", Constants.currentUser)
                .putParcelableArrayListExtra("questions_list", Constants.currentUserQuestion)
                .putExtra("User_Mode",userMode)
                    .putExtra("total",1))
            (context as Activity).overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

    }

    inner class ViewHolder(val binding : ItemDesignCheckInLayoutBinding) : RecyclerView.ViewHolder(binding.root)

}