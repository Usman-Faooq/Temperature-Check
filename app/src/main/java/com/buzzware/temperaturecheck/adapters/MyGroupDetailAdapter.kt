package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ItemDesignGroupMembersBinding
import com.buzzware.temperaturecheck.model.UserModel

class MyGroupDetailAdapter(val context : Context,val arrayList : ArrayList<UserModel>,val listener : OnClicked) : RecyclerView.Adapter<MyGroupDetailAdapter.ViewHolder>() {


    interface OnClicked{
        fun OnItemClikced(id: String)
        fun OnMessageIconClicked(id: String, userName: String)
        fun OnDeleteIconClicked(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignGroupMembersBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayList[position]

        holder.binding.userName.text = list.userName


        Glide.with(context)
            .load(list.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.userRoundedImageView)

        holder.binding.root.setOnClickListener {
            listener.OnItemClikced(list.id)
        }
        holder.binding.chatIV.setOnClickListener {
            listener.OnMessageIconClicked(list.id,list.userName)
        }
        holder.binding.deleteIV.setOnClickListener {
            listener.OnDeleteIconClicked(list.id)
            arrayList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }



    inner class ViewHolder(val binding : ItemDesignGroupMembersBinding) : RecyclerView.ViewHolder(binding.root)


}