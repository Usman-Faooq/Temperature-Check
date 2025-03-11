package com.buzzware.temperaturecheck.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.MyGroupDetailAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityMyGroupDetailBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.util.Calendar
import java.util.UUID

class MyGroupDetail : BaseActivity() , MyGroupDetailAdapter.OnClicked{

    private val binding : ActivityMyGroupDetailBinding by lazy {
        ActivityMyGroupDetailBinding.inflate(layoutInflater)
    }

    private val questionArray : ArrayList<UserQuestionModel> = arrayListOf()

    private val progressList = ArrayList<Int>()
    private lateinit var groupName : String
    private lateinit var groupID : String
    private lateinit var myGroupDetailAdapter: MyGroupDetailAdapter
    private var groupMembersData : ArrayList<UserModel> = ArrayList<UserModel>()
    private var groupMembers : HashMap<String,String> = hashMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Constants.communityUserId.clear()

        groupName = intent.getStringExtra("groupName") ?: ""
        groupID = intent.getStringExtra("groupID") ?: ""
        groupMembers = intent.getSerializableExtra("groupMembers") as? HashMap<String, String> ?: hashMapOf()

        Constants.communityUserId.addAll(groupMembers.keys)

        groupMembers = getAcceptedMembers(groupMembers)



        setViews()
        setListener()
        getGroupMembers()


    }

    fun getAcceptedMembers(communityMap: HashMap<String, String>): HashMap<String, String> {
        return communityMap.filter { it.value == "accepted" } as HashMap<String, String>


    }

    private fun getGroupMembers() {

        if (groupMembers.isNotEmpty())
        {
            mDialog.show()
            groupMembers.keys.forEachIndexed { index, it ->
                db.collection("Users").document(it)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val model = document.toObject(UserModel::class.java)!!

                            // Wait until getTodayUserQuestions() completes before adding model
                            getTodayUserQuestions(it) { average ->
                                model.userprogress = average.toLong()
                                // Now add the model to the list
                                groupMembersData.add(model)

                                // Only update adapter when all documents are fetched
                                if (groupMembersData.size == groupMembers.keys.size) {
                                    updateAdapter()
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        showAlert(exception.message)
                        mDialog.dismiss()
                    }
            }

        }else
        {
            binding.noMember.visibility = View.VISIBLE
        }
    }


    fun updateAdapter() {

        binding.groupRecycler.layoutManager = LinearLayoutManager(baseContext)
        myGroupDetailAdapter = MyGroupDetailAdapter(this, groupMembersData, this)
        binding.groupRecycler.adapter = myGroupDetailAdapter
        mDialog.dismiss()
    }


    private fun setListener(){

        binding.backIV.setOnClickListener { finish() }

        binding.communityPerformance.setOnClickListener {
            startActivity(Intent(this, CommunityGraphActivity::class.java))
        }

        binding.addMembers.setOnClickListener {

            checkGroupLimit(groupID){groupCount ->
                if (!groupCount){
                    showAlert("You have reached maximum numbers of group members!")
                }else{
                    showAlertDialog()
                }

            }

        }

    }

    private fun showAlertDialog() {
        val options = arrayOf("Invite via link", "Invite from community")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an Option")
            .setCancelable(true)
            .setItems(options) { _, which ->
                when (options[which]) {
                    "Invite via link" -> {
                        val inviteUrl = "https://tempchecker1.page.link/joincom2?userId=${Constants.currentUser.id}&groupId=$groupID"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, inviteUrl) }
                        startActivity(Intent.createChooser(intent, "Share via"))
                    }
                    "Invite from community" -> {
                        val intent = Intent(this,InviteCommunityMembers::class.java)
                        intent.putExtra("groupID","${groupID}")
                        startActivity(intent)
                        overridePendingTransition(fadeIn,fadeOut)
                    }
                }
            }

        val dialog = builder.create()
        dialog.show()
    }



    private fun checkGroupLimit(groupId: String, callback: (Boolean) -> Unit) {
        db.collection("Groups")
            .document(groupId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val groupCount = document.getLong("groupCount") ?: 0

                    if (groupCount >= 5) {
                        callback(false)
                    } else {
                        callback(true)
                    }
                } else {
                    callback(true)
                }
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }


    fun getTodayUserQuestions(userId: String, callback: (Int) -> Unit) {
        val progressList = mutableListOf<Int>()
        val db = FirebaseFirestore.getInstance()
        val currentTimeStamp = System.currentTimeMillis()

        db.collection("UserQuestions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val model = result.toObjects(UserQuestionModel::class.java)

                model.forEach {
                    if (isSameDay(currentTimeStamp, it.date)) {
                        when (it.answer) {
                            "Very Happy" -> progressList.add(5)
                            "Happy" -> progressList.add(4)
                            "Uneasy" -> progressList.add(3)
                            "Sad" -> progressList.add(2)
                            "Angry" -> progressList.add(1)
                        }
                    }
                }

                val avg = if (progressList.isNotEmpty()) progressList.sum() / progressList.size else 0
                callback(avg)
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
                callback(0)
            }
    }





    private fun setViews() {

        binding.titleTV.text = groupName.toString()

    }

    override fun OnItemClikced(model: UserModel) {
        mDialog.show()
        Log.d("LOGER", "interface clicked")
        var userMode = 0
        var countSize = 0
        db.collection("UserQuestions")
            .whereEqualTo("userId", model.id)
            .get()
            .addOnSuccessListener { query ->

                query.forEach { doc ->
                    val questionModel = doc.toObject(UserQuestionModel::class.java)
                    questionModel.id = doc.id
                    questionArray.add(questionModel)

                    userMode += getMoodScore(questionModel.answer)

                    countSize++
                }

                mDialog.dismiss()
                if (countSize == query.size()){
                    val intent = Intent(this, MyTempResultActivity::class.java)
                    intent.putExtra("userModel", model)
                    intent.putParcelableArrayListExtra("questions_list", questionArray)
                    intent.putExtra("User_Mode", userMode)
                    intent.putExtra("total", countSize)
                    startActivity(intent)
                    overridePendingTransition(fadeIn, fadeOut)
                }
            }

    }

    override fun OnMessageIconClicked(id: String, userName: String) {
        mDialog.show()
        findChatByParticipants(Constants.currentUser.id,id){chatID ->
            if (chatID.isNotEmpty()){
                mDialog.dismiss()
                val intent = Intent(this,ChatScreenActivity::class.java)
                intent.putExtra("ChatID","${chatID}")
                intent.putExtra("chatName","${userName}")
                intent.putExtra("SenderID","${id}")
                intent.putExtra("chatType","one")
                startActivity(intent)


            }else{
                mDialog.dismiss()
                val intent = Intent(this,ChatScreenActivity::class.java)
                intent.putExtra("ChatID","${UUID.randomUUID()}")
                intent.putExtra("chatName","${userName}")
                intent.putExtra("SenderID","${id}")
                intent.putExtra("chatType","one")
                startActivity(intent)

            }

        }
    }

    override fun OnDeleteIconClicked(id: String) {

        mDialog.show()
        val deleteUser = hashMapOf<String,Any>(
            "comunity.${id}" to FieldValue.delete()
        )

        db.collection("Groups")
            .document(groupID)
            .update(deleteUser)
            .addOnSuccessListener {
                showToast("User Removed!")
                mDialog.dismiss()
                decreamentGroupCount()
            }
            .addOnFailureListener { exception -> showAlert(exception.message)
            mDialog.dismiss()}

    }

    private fun decreamentGroupCount() {

        db.collection("Groups")
            .document(groupID)
            .update("groupCount",FieldValue.increment(-1))
            .addOnSuccessListener {  }
            .addOnFailureListener { exception->
                showAlert(exception.message)
            }

    }



    fun findChatByParticipants(userId1: String, userId2: String, callback : (String) -> Unit){
        db.collection("Chat")
            .whereEqualTo("participants.$userId1", true)
            .whereEqualTo("participants.$userId2", true)
            .whereEqualTo("chatType","one")
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

    private fun getMoodScore(answer: String?): Int {
        return when (answer) {
            "Very Happy" -> 5
            "Happy" -> 4
            "Uneasy" -> 3
            "Sad" -> 2
            "Angry" -> 1
            else -> 0
        }
    }

}