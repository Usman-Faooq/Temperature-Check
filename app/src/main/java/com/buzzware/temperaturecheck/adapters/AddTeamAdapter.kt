package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ItemDesignCheckInLayoutBinding
import com.buzzware.temperaturecheck.databinding.ItemDesignTeamLayoutBinding

class AddTeamAdapter(val context: Context, val list: ArrayList<String>) : RecyclerView.Adapter<AddTeamAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemDesignTeamLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignTeamLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == 4){
            holder.binding.rootIV.setImageResource(R.drawable.iv_add)
        }

    }
}