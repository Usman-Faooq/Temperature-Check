package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.MyGroupDetailAdapter
import com.buzzware.temperaturecheck.databinding.ActivityMyGroupDetailBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.google.firebase.firestore.FieldValue
import java.util.UUID

class MyGroupDetail : BaseActivity() , MyGroupDetailAdapter.OnClicked{

    private val binding : ActivityMyGroupDetailBinding by lazy {
        ActivityMyGroupDetailBinding.inflate(layoutInflater)
    }

    private lateinit var groupName : String
    private lateinit var groupID : String
    private lateinit var myGroupDetailAdapter: MyGroupDetailAdapter
    private var groupMembersData : ArrayList<UserModel> = ArrayList<UserModel>()
    private var groupMembers : HashMap<String,String> = hashMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        groupName = intent.getStringExtra("groupName") ?: ""
        groupID = intent.getStringExtra("groupID") ?: ""
        groupMembers = intent.getSerializableExtra("groupMembers") as? HashMap<String, String> ?: hashMapOf()


        setViews()
        setListener()





    }


    private fun setListener(){

        binding.backIV.setOnClickListener { finish() }

        binding.communityPerformance.setOnClickListener {
            startActivity(Intent(this,InviteCommunityMembers::class.java))
            overridePendingTransition(fadeIn,fadeOut)
        }

    }

    private fun setViews() {

        binding.titleTV.text = groupName.toString()


        if (groupMembers.isNotEmpty())
        {
            groupMembers.keys.forEach {
                db.collection("Users").document(it)
                    .get().addOnSuccessListener {document ->
                        if (document.exists())
                        {
                            groupMembersData.add(document?.toObject(UserModel::class.java)!!)
                            val linearLayoutManager = LinearLayoutManager(baseContext)
                            binding.groupRecycler.layoutManager = linearLayoutManager
                            myGroupDetailAdapter= MyGroupDetailAdapter(baseContext,groupMembersData,this)
                            binding.groupRecycler.adapter = myGroupDetailAdapter
                        }

                    }
                    .addOnFailureListener { exception->
                        showAlert(exception.message)
                    }
            }
        }else
        {
            binding.noMember.visibility = View.VISIBLE
        }

    }

    override fun OnItemClikced(id: String) {

    }

    override fun OnMessageIconClicked(id: String, userName: String) {
        findChatByParticipants("cJsN8ylqXTRl1TeV4dRTS2M3Y7E3",id){chatID ->
            if (chatID.isNotEmpty()){
                val intent = Intent(this,ChatScreenActivity::class.java)
                intent.putExtra("ChatID","${chatID}")
                intent.putExtra("chatName","${userName}")
                intent.putExtra("SenderID","${id}")
                startActivity(intent)

            }else {
                val intent = Intent(this,ChatScreenActivity::class.java)
                intent.putExtra("ChatID","${UUID.randomUUID()}")
                intent.putExtra("chatName","${userName}")
                intent.putExtra("SenderID","${id}")
                startActivity(intent)

            }

        }
    }

    override fun OnDeleteIconClicked(id: String) {

        val deleteUser = hashMapOf<String,Any>(
            "comunity.${id}" to FieldValue.delete()
        )

        db.collection("Groups")
            .document(groupID)
            .update(deleteUser)
            .addOnSuccessListener {
                showToast("User Removed!")
            }
            .addOnFailureListener { exception -> showAlert(exception.message) }

    }


    fun findChatByParticipants(userId1: String, userId2: String, callback : (String) -> Unit){

        db.collection("Chat")
            .whereEqualTo("participants.$userId1", true)
            .whereEqualTo("participants.$userId2", true)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                       callback(document.id)

                    }
                } else {
                   callback("")
                }
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
                callback("")
            }
    }

}