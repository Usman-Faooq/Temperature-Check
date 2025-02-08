package com.buzzware.temperaturecheck.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.activities.MyTempResultActivity
import com.buzzware.temperaturecheck.activities.TempResultActivity
import com.buzzware.temperaturecheck.databinding.ItemDesignCheckInLayoutBinding

class CheckInAdapter(val context: Context, val list: ArrayList<String>) : RecyclerView.Adapter<CheckInAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDesignCheckInLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignCheckInLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.progressSlider.setOnTouchListener { _, _ -> true }

        holder.binding.root.setOnClickListener {
            context.startActivity(Intent(context, MyTempResultActivity::class.java))
            (context as Activity).overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

    }
}