package com.buzzware.temperaturecheck.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.TempResultActivity
import com.buzzware.temperaturecheck.databinding.ItemDesignCheckInLayoutBinding
import com.buzzware.temperaturecheck.databinding.ItemDesignCommunityLayoutBinding

class CommunityAdapter(val context: Context, val list: ArrayList<String>) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDesignCommunityLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignCommunityLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.binding.root.setOnClickListener {
            context.startActivity(Intent(context, TempResultActivity::class.java))
            (context as Activity).overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }

    }
}