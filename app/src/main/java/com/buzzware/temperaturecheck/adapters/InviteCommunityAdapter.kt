package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.PersonalChatItemDesignBinding
import com.buzzware.temperaturecheck.databinding.SearchCommunityMemberItemDesignBinding
import com.buzzware.temperaturecheck.model.UserModel

class InviteCommunityAdapter(val context : Context, val arraylist : ArrayList<UserModel>) : RecyclerView.Adapter<InviteCommunityAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteCommunityAdapter.ViewHolder {
        return ViewHolder(SearchCommunityMemberItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arraylist[position]

        holder.binding.userName.text = list.userName

        Glide.with(context)
            .load(list.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.userRoundedImageView)
    }

    inner class ViewHolder(val binding : SearchCommunityMemberItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}