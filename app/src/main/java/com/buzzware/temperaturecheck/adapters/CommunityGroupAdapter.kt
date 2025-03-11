package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.databinding.GroupCommunityItemDesignBinding
import com.buzzware.temperaturecheck.model.GroupModel

class CommunityGroupAdapter(val context : Context, val arrayList: ArrayList<GroupModel>, val listener : OnClicked ) : RecyclerView.Adapter<CommunityGroupAdapter.ViewHolder>() {


    interface OnClicked{
        fun OnItemClicked(id: String, name: String, comunity: HashMap<String, String>)
        fun OnLeaveClicked(name: String, id: String)
        fun OnMessageIconClicked(name: String, id: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GroupCommunityItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayList[position]

        holder.binding.groupName.text = list.name


        holder.binding.root.setOnClickListener {
            listener.OnItemClicked(list.id,list.name,list.comunity)
        }

        holder.binding.leftIV.setOnClickListener {
            listener.OnLeaveClicked(list.name,list.id)
        }

        holder.binding.chatIV.setOnClickListener {
            listener.OnMessageIconClicked(list.name,list.id)
        }

    }

    inner class ViewHolder(val binding : GroupCommunityItemDesignBinding) : RecyclerView.ViewHolder(binding.root)

}