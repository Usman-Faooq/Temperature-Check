package com.buzzware.temperaturecheck.activities

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.MessagingScreenAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityChatScreenBinding
import com.buzzware.temperaturecheck.model.MessagesModel
import com.google.firebase.firestore.Query
import java.util.UUID

class ChatScreenActivity : BaseActivity() {
    private val binding : ActivityChatScreenBinding by lazy {
        ActivityChatScreenBinding.inflate(layoutInflater)
    }
    var chatId : String = ""
    var chatName : String = ""
    var senderId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatId = intent.getStringExtra("ChatID") ?: ""
        chatName = intent.getStringExtra("chatName") ?: ""
        senderId = intent.getStringExtra("SenderID") ?: ""

        showAlert("${chatId  +  chatName  +  senderId}")


        setViews()
        setListener()






    }

    private fun setListener() {

        binding.backIV.setOnClickListener {
            finish()
            overridePendingTransition(fadeIn,fadeOut)
        }

        binding.sendIV.setOnClickListener {
            if (binding.messageTypingET.text.isNotEmpty())
            {
                sendMessage(binding.messageTypingET.text.toString() , UUID.randomUUID().toString())
                binding.messageTypingET.setText("")

            }
        }

    }

    private fun sendMessage(message: String, UID: String) {
        val chatRef = db.collection("Chat").document(chatId) // Reference to chat document

        chatRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val newChatData = hashMapOf(
                    "chatType" to "one",
                    "id" to chatId,
                    "participants" to hashMapOf(
                        "cJsN8ylqXTRl1TeV4dRTS2M3Y7E3" to true,
                        senderId to true
                    ),
                    "lastMessage" to "",
                    "timestamp" to System.currentTimeMillis()
                )

                chatRef.set(newChatData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "New chat document created")
                        sendMessageToConversation(message, UID) // Send the message after chat is created
                    }
                    .addOnFailureListener { exception ->
                        showAlert("Failed to create chat: ${exception.message}")
                    }
            } else {
                sendMessageToConversation(message, UID)
            }
        }.addOnFailureListener { exception ->
            showAlert("Error checking chat: ${exception.message}")
        }
    }

    // Function to send message in Conversations sub-collection
    private fun sendMessageToConversation(message: String, UID: String) {
        val data = hashMapOf(
            "content" to message,
            "fromID" to "cJsN8ylqXTRl1TeV4dRTS2M3Y7E3",
            "isRead" to hashMapOf("cJsN8ylqXTRl1TeV4dRTS2M3Y7E3" to true, senderId to false),
            "messageId" to UID,
            "timestamp" to System.currentTimeMillis(),
            "toID" to senderId,
            "type" to "text",
        )

        db.collection("Chat").document(chatId)
            .collection("Conversations")
            .document(UID)
            .set(data)
            .addOnSuccessListener {
                db.collection("Chat")
                    .document(chatId)
                    .update("lastMessage", data)
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {exception ->
                        showAlert(exception.message)
                    }
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }
    }

    private fun setViews(){

        binding.titleTV.text = chatName.toString()


        db.collection("Chat").document(chatId)
            .collection("Conversations")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->

                var model = value?.toObjects(MessagesModel::class.java) as ArrayList

                Log.d("LOGGER1","${model}")
                val messagingScreenAdapter = MessagingScreenAdapter(applicationContext,model)
                val linearlayout = LinearLayoutManager(applicationContext)
                linearlayout.stackFromEnd = true
                linearlayout.reverseLayout = false
                binding.chatingRecyclerView.layoutManager = linearlayout
                binding.chatingRecyclerView.adapter = messagingScreenAdapter


            }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}