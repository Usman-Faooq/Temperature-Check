package com.buzzware.temperaturecheck.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.MessagingScreenAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityChatScreenBinding
import com.buzzware.temperaturecheck.model.MessagesModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.io.Serializable
import java.util.UUID

class ChatScreenActivity : BaseActivity() {
    private val binding : ActivityChatScreenBinding by lazy {
        ActivityChatScreenBinding.inflate(layoutInflater)
    }
    var messagingArrayList : ArrayList<MessagesModel> = ArrayList()
    var chatId : String = ""
    var chatName : String = ""
    var senderId : String = ""
    var chatType : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        chatId = intent.getStringExtra("ChatID") ?: ""
        chatName = intent.getStringExtra("chatName") ?: ""
        senderId = intent.getStringExtra("SenderID") ?: ""
        chatType = intent.getStringExtra("chatType") ?: ""


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
                sendMessage(
                    binding.messageTypingET.text.toString(),
                    UUID.randomUUID().toString(),
                    "text"
                )
                binding.messageTypingET.setText("")
            }
        }

        binding.galleryIV.setOnClickListener {
            ImagePicker.with(this)
                .galleryMimeTypes(arrayOf("image/*", "image/gif")) // Allow images & GIFs
                .start()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            val fileUri: Uri = data?.data!!
            mDialog.show()

            // Check if the file is a GIF or an image
            val mimeType = contentResolver.getType(fileUri)

            if (mimeType == "image/gif") {
                // Handle GIF upload
                uploadMediaToFirebaseStorage(fileUri, UUID.randomUUID().toString(), "gif")
            } else {
                // Handle Image upload
                mDialog.show()
                uploadMediaToFirebaseStorage(fileUri, UUID.randomUUID().toString(), "image")
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMediaToFirebaseStorage(fileUri: Uri, UID: String, textType: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "Users/chating/${Constants.currentUser.id}.${textType}"
        val mediaRef = storageRef.child(fileName)

        val uploadTask = mediaRef.putFile(fileUri)
        uploadTask.addOnSuccessListener {
            mediaRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                downloadUrl.toString()
                sendMessage(downloadUrl.toString(),UID,textType)
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Upload Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            mDialog.dismiss()
        }
    }

    private fun sendMessage(message: String, UID: String, textType : String) {

        val data = hashMapOf(
            "content" to message,
            "fromID" to Constants.currentUser.id,
            "isRead" to hashMapOf(Constants.currentUser.id to true, senderId to true),
            "messageId" to UID,
            "timestamp" to System.currentTimeMillis(),
            "toID" to senderId,
            "type" to textType,
        )

        val chatRef = db.collection("Chat").document(chatId)

        chatRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                // If chat doesn't exist, create it with participants
                val newChatData = mapOf(
                    "chatType" to chatType,
                    "groupName" to chatName,
                    "id" to chatId,
                    "participants" to hashMapOf("${Constants.currentUser.id}" to true,senderId to true),
                    "lastMessage" to data,
                    "timestamp" to System.currentTimeMillis()
                )

                chatRef.set(newChatData)
                    .addOnSuccessListener {
                        sendMessageToConversation(UID, data)
                        mDialog.dismiss()
                    }
                    .addOnFailureListener { exception ->
                        showAlert("Failed to create chat: ${exception.message}")
                        mDialog.dismiss()
                    }
            } else {
                // Chat exists, check if currentUser is in participants
                val participants = document.get("participants") as? Map<String, Boolean>

                if (participants == null || !participants.containsKey(Constants.currentUser.id) && chatType == "many") {
                    // If the user is not in participants, add them
                    chatRef.update("participants", participants?.plus(Constants.currentUser.id to true) ?: mapOf(Constants.currentUser.id to true))
                        .addOnSuccessListener {
                            Log.d("Firestore", "User added to participants")
                            sendMessageToConversation(UID, data) // Send message after adding participant
                            mDialog.dismiss()
                        }
                        .addOnFailureListener { exception ->
                            showAlert("Failed to add participant: ${exception.message}")
                            mDialog.dismiss()
                        }
                } else {
                    // User is already a participant, just send the message
                    sendMessageToConversation(UID, data)
                    mDialog.dismiss()
                }
            }
        }.addOnFailureListener { exception ->
            showAlert("Error checking chat: ${exception.message}")
            mDialog.dismiss()
        }
    }




    // Function to send message in Conversations sub-collection
    private fun sendMessageToConversation(UID: String, data: HashMap<String, Serializable>) {


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
        messagingArrayList.clear()

        binding.titleTV.text = chatName.toString()
        mDialog.show()
        db.collection("Chat").document(chatId)
            .collection("Conversations")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->

                if (error?.message != null)
                {
                   showAlert(error.message)
                   mDialog.dismiss()
                   return@addSnapshotListener
                }

                messagingArrayList = value?.toObjects(MessagesModel::class.java) as ArrayList
                val messagingScreenAdapter = MessagingScreenAdapter(applicationContext,messagingArrayList)
                val linearlayout = LinearLayoutManager(applicationContext)
                linearlayout.stackFromEnd = true
                linearlayout.reverseLayout = false
                binding.chatingRecyclerView.layoutManager = linearlayout
                binding.chatingRecyclerView.adapter = messagingScreenAdapter
                mDialog.dismiss()


            }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}