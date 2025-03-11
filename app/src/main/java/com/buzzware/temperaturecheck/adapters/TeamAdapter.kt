package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ItemDesignTeamLayoutBinding
import com.buzzware.temperaturecheck.model.UserModel

class TeamAdapter(val context: Context, val list: ArrayList<UserModel>, val listener : OnUserClickedListener) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    interface OnUserClickedListener{
        fun communityUserClick(model : UserModel)
    }

    inner class ViewHolder(val binding : ItemDesignTeamLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignTeamLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        Glide.with(context)
            .load(model.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.rootIV)

        holder.binding.rootIV.setOnClickListener {
            listener.communityUserClick(model)
        }
    }
}