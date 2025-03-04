package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.databinding.ChatingItemDesignBinding
import com.buzzware.temperaturecheck.model.MessagesModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessagingScreenAdapter(val context : Context,val arraylist : ArrayList<MessagesModel>) : RecyclerView.Adapter<MessagingScreenAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ChatingItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arraylist[position]

        if (list.fromID.equals("cJsN8ylqXTRl1TeV4dRTS2M3Y7E3"))
        {
            holder.binding.recieverItem.visibility = View.INVISIBLE
            holder.binding.recieverItemTime.visibility = View.INVISIBLE
            holder.binding.senderItem.text = list.content
            holder.binding.senderItemTime.text = formatTimestamp(list.timestamp)
        } else
        {
            holder.binding.senderItem.visibility = View.INVISIBLE
            holder.binding.senderItemTime.visibility = View.INVISIBLE
            holder.binding.recieverItem.text = list.content
            holder.binding.recieverItemTime.text = formatTimestamp(list.timestamp)
        }
    }


    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }


    inner class ViewHolder(val binding : ChatingItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}