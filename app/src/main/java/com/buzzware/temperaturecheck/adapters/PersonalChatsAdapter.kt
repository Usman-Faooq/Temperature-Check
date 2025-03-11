package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.PersonalChatItemDesignBinding
import com.buzzware.temperaturecheck.model.ChatModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PersonalChatsAdapter(
    val context: Context?,
    val personalChatData: ArrayList<ChatModel>,
    val listener : OnClicked
)  : RecyclerView.Adapter<PersonalChatsAdapter.ViewHolder>()
{
    interface OnClicked{
        fun onItemClicked(chatId: String?, s: String, senderID: String?)
        fun onChatDeleteClicked(chatId: String?)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PersonalChatItemDesignBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return personalChatData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list1 = personalChatData[position]
        holder.binding.messageTitle.text = list1.lastMessage?.content.toString()

        list1.participants?.keys?.forEach {
            if (Constants.currentUser.id != it)
            {
                list1.senderID = it
                getPersonalSenderData(it){userName ->

                    holder.binding.SenderName.text = userName
                }
            }
        }

        holder.binding.root.setOnClickListener {
            listener.onItemClicked(list1.chatId,holder.binding.SenderName.text.toString() ?: "",list1.senderID)
        }

        holder.binding.delete.setOnClickListener {
            listener.onChatDeleteClicked(list1.chatId)
        }
    }


    private fun getPersonalSenderData(id: String, callback: (String) -> Unit) {
        FirebaseFirestore.getInstance().collection("Users")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val username = document.getString("firstName") + " " + document.getString("lastName")
                callback(username)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting document", exception)
            }
    }


    inner class ViewHolder(val binding : PersonalChatItemDesignBinding) : RecyclerView.ViewHolder(binding.root)




}