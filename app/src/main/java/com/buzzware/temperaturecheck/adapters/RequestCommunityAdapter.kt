package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.CommunityRequestItemDesignBinding
import com.buzzware.temperaturecheck.model.GroupModel

class RequestCommunityAdapter(val context: Context , val arrayList : ArrayList<GroupModel>,val listener : OnAccepted) : RecyclerView.Adapter<RequestCommunityAdapter.ViewHolder>() {


    interface OnAccepted
    {
        fun OnAcceptedClicked(id: String, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CommunityRequestItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list =  arrayList[position]

        holder.binding.userName.text = list.userName
        holder.binding.groupName.text = list.name


        Glide.with(context)
            .load(list.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.userRoundedImageView)



        holder.binding.acceptedTV.setOnClickListener {
            listener.OnAcceptedClicked(list.id,position)
            arrayList.removeAt(position)
            notifyDataSetChanged()
        }
    }


    inner class ViewHolder(val binding : CommunityRequestItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}