package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
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

        if (list.fromID == Constants.currentUser.id) {

            holder.binding.myLayout.visibility = View.VISIBLE
            holder.binding.reciveLayout.visibility = View.GONE

            holder.binding.senderItem.text = list.content
            holder.binding.senderItemTime.text = formatTimestamp(list.timestamp)

            when(list.type) {
                "image"->{
                    holder.binding.senderItem.visibility = View.GONE
                    holder.binding.senderImage.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(list.content)
                        .into(holder.binding.senderImage)
                }
                "gif"->{
                    holder.binding.senderItem.visibility = View.GONE
                    holder.binding.senderImage.visibility = View.VISIBLE
                    Glide.with(context)
                        .asGif()
                        .load(list.content)
                        .into(holder.binding.senderImage)}
                else ->{
                    holder.binding.senderItem.visibility = View.VISIBLE
                    holder.binding.senderImage.visibility = View.GONE
                }
            }
        } else {

            holder.binding.myLayout.visibility = View.GONE
            holder.binding.reciveLayout.visibility = View.VISIBLE

            holder.binding.recieverItem.text = list.content
            holder.binding.recieverItemTime.text = formatTimestamp(list.timestamp)

            when(list.type) {
                "image" -> {
                    holder.binding.recieverItem.visibility = View.GONE
                    holder.binding.recieverImage.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(list.content)
                        .into(holder.binding.recieverImage)
                }
                "gif" -> {
                    holder.binding.recieverItem.visibility = View.GONE
                    holder.binding.recieverImage.visibility = View.VISIBLE
                    Glide.with(context)
                        .asGif()
                        .load(list.content)
                        .into(holder.binding.recieverImage)
                }
                else -> {
                    holder.binding.recieverItem.visibility = View.VISIBLE
                    holder.binding.recieverImage.visibility = View.GONE
                }
            }
        }
    }


    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }


    inner class ViewHolder(val binding : ChatingItemDesignBinding) : RecyclerView.ViewHolder(binding.root)



}