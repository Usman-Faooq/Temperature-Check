package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.adapters.InviteCommunityAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityInviteCommunityMembersBinding
import com.buzzware.temperaturecheck.fragments.MyGroupCommunity
import com.buzzware.temperaturecheck.model.UserModel
import com.google.firebase.firestore.FieldValue

class InviteCommunityMembers : BaseActivity() , InviteCommunityAdapter.OnClicked{

    private val binding : ActivityInviteCommunityMembersBinding by lazy {
        ActivityInviteCommunityMembersBinding.inflate(layoutInflater)
    }
    var communityList : ArrayList<UserModel> = ArrayList()
    private lateinit var groupID : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        groupID = intent.getStringExtra("groupID") ?: ""


        setViews()
        setListener()


        setContentView(binding.root)

    }

    private fun setListener() {
        binding.userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // Clear the list if the search input is empty
                    communityList.clear()
                    binding.communityRecycler.visibility = View.GONE
                    binding.communityRecycler.visibility = View.VISIBLE
                } else {

                    performSearch(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setViews() {

    }


    private fun performSearch(queryText: String) {

        val endText = "$queryText\uf8ff"
        db.collection("Users")
            .orderBy("firstName")
            .whereGreaterThanOrEqualTo("firstName", queryText)
            .whereLessThanOrEqualTo("firstName", endText)
            .addSnapshotListener { queryDocumentSnapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                queryDocumentSnapshots?.let {
                    communityList.clear()
                    communityList.addAll(it.toObjects(UserModel::class.java))
                    val linearLayoutManager = LinearLayoutManager(this)
                    binding.communityRecycler.layoutManager = linearLayoutManager
                    binding.communityRecycler.adapter = InviteCommunityAdapter(this,communityList,this,groupID)

                }
            }
    }



    override fun OnItemClicked(id: String) {

        mDialog.show()
        db.collection("Groups")
            .document(groupID)
            .update("comunity.${id}","requested")
            .addOnSuccessListener {
                updateGRoupNumber()
                finish()
                mDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
                mDialog.dismiss()
            }

    }

    private fun updateGRoupNumber() {
        db.collection("Groups")
            .document(groupID)
            .update("groupCount",FieldValue.increment(1))
            .addOnSuccessListener {
                mDialog.dismiss()
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
                mDialog.dismiss()
            }

    }

}