package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.databinding.PersonalChatItemDesignBinding
import com.buzzware.temperaturecheck.fragments.BaseFragment
import com.buzzware.temperaturecheck.model.ChatModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GroupChatingAdapter(val context: Context?, val arrayList : ArrayList<ChatModel>,val listener : OnClicked)
    : RecyclerView.Adapter<GroupChatingAdapter.ViewHolder>() {

    interface OnClicked{
        fun OnItemClicked(chatId: String?, toString: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PersonalChatItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayList[position]
        holder.binding.SenderName.text = list.groupName
        holder.binding.messageTitle.text = list.lastMessage?.content



        holder.binding.root.setOnClickListener {
            listener.OnItemClicked(list.chatId,holder.binding.SenderName.text.toString())
        }

        holder.binding.delete.setOnClickListener {
            LeaveGroup(list.chatId)
        }

    }

    private fun LeaveGroup(chatId: String?) {

        val updates = hashMapOf<String, Any>(
            "participants.cJsN8ylqXTRl1TeV4dRTS2M3Y7E3" to FieldValue.delete()
        )

        FirebaseFirestore.getInstance().collection("Chat")
            .document(chatId!!)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "You left the group", Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->

            }


    }


    inner class ViewHolder(val binding : PersonalChatItemDesignBinding) : RecyclerView.ViewHolder(binding.root)


}