package com.buzzware.temperaturecheck.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.activities.ChatScreenActivity
import com.buzzware.temperaturecheck.adapters.PersonalChatsAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentPersonalChatBinding
import com.buzzware.temperaturecheck.model.ChatModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class PersonalChatFragment() : BaseFragment(),PersonalChatsAdapter.OnClicked {
    val binding: FragmentPersonalChatBinding by lazy {
        FragmentPersonalChatBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        getChatingData()



        return binding.root
    }

    private fun getChatingData() {

        db.collection("Chat")
            .whereEqualTo("participants.${Constants.currentUser.id}", true)
            .addSnapshotListener { document, error ->

                if (error != null){
                    showAlert("Error: ${error.message}")
                    return@addSnapshotListener
                }

                Constants.userPersonalChats.clear()
                Constants.userGroupChats.clear()


                    document?.forEach {
                        val model = it.toObject(ChatModel::class.java)
                        model.chatId = it.id
                        if (model.chatType == "one") {
                            Constants.userPersonalChats.add(model)
                        } else {
                            Constants.userGroupChats.add(model)
                        }
                    }
                    setViews()

            }


    }

    private fun setViews() {

        Constants.userPersonalChats.sortByDescending { it.lastMessage.timestamp }

        val personalChatsAdapter = PersonalChatsAdapter(context, Constants.userPersonalChats,this)
        binding.personalChat.layoutManager = LinearLayoutManager(context)
        binding.personalChat.adapter = personalChatsAdapter
        mDialog.dismiss()

    }

    override fun onItemClicked(chatId: String?, chatName: String, senderID: String?) {
        val intent = Intent(context,ChatScreenActivity::class.java)
        intent.putExtra("ChatID",chatId)
        intent.putExtra("chatName", chatName)
        intent.putExtra("SenderID", senderID)
        startActivity(intent)
        requireActivity().overridePendingTransition(fadeIn, fadeOut)

    }

    override fun onChatDeleteClicked(chatId: String?) {
        leaveGroup(chatId)
    }

    private fun leaveGroup(chatId: String?) {

        val updates = hashMapOf<String, Any>(
            "participants.${Constants.currentUser.id}" to FieldValue.delete()
        )
        FirebaseFirestore.getInstance().collection("Chat")
            .document(chatId!!)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "You left the group", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }
    }

}