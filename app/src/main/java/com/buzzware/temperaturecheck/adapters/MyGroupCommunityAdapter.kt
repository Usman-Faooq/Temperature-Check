package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.databinding.MyGroupCommunityItemDesignBinding
import com.buzzware.temperaturecheck.model.GroupModel

class MyGroupCommunityAdapter(val context : Context, val arrayList : ArrayList<GroupModel>, val listener : OnClicked) : RecyclerView.Adapter<MyGroupCommunityAdapter.ViewHolder>() {


    interface OnClicked{
        fun OnItemClicked(name: String, id: HashMap<String, String>, id1: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyGroupCommunityItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayList[position]
        holder.binding.groupName.text = list.name


        holder.binding.root.setOnClickListener {
            listener.OnItemClicked(list.name,list.comunity,list.id)
        }
    }


    inner class ViewHolder(val binding : MyGroupCommunityItemDesignBinding) : RecyclerView.ViewHolder(binding.root)
}