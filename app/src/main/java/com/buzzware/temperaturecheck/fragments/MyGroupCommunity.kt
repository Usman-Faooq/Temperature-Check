package com.buzzware.temperaturecheck.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.activities.ChatScreenActivity
import com.buzzware.temperaturecheck.activities.MyGroupDetail
import com.buzzware.temperaturecheck.adapters.MyGroupCommunityAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.EditNameCustomAlertBinding
import com.buzzware.temperaturecheck.databinding.FragmentMyGroupCommunityBinding
import com.buzzware.temperaturecheck.model.GroupModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class MyGroupCommunity : BaseFragment(),MyGroupCommunityAdapter.OnClicked {

    private val binding : FragmentMyGroupCommunityBinding by lazy {
        FragmentMyGroupCommunityBinding.inflate(layoutInflater)
    }

    private lateinit var context : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View{


        setViews()
        setListener()


        return binding.root
    }

    private fun setListener() {

        binding.addTeam.setOnClickListener {

            checkGroupCount {isGroupNumbermax ->
                if (isGroupNumbermax){
                    BottomFragmentAddGroup().show(parentFragmentManager,"")
                }

            }
        }

    }

    private fun setViews(){
        getMyGroupData()
    }

    private fun checkGroupCount(callback : (Boolean) -> Unit)
    {
        if (Constants.currentUser.groupCount >= 5)
        {
            showAlert("You have reached maximun number of groups!")
            callback(false)
        }
        else{
            callback(true)
        }
    }
    private fun getMyGroupData()
    {
        db.collection("Groups")
            .whereEqualTo("userId","${Constants.currentUser.id}")
            .addSnapshotListener { value, error ->
                if (error != null)
                {
                    showAlert(error.message)
                }else{
                    val model = value?.toObjects(GroupModel::class.java) as ArrayList<GroupModel>
                    model.sortedByDescending { it.date }
                    val linearLayoutManager = LinearLayoutManager(context)
                    binding.myGroupRecycler.layoutManager = linearLayoutManager
                    binding.myGroupRecycler.adapter = MyGroupCommunityAdapter(context,model,this)

                }

            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun OnItemClicked(name: String, community: HashMap<String, String>, groupID: String) {

        val intent = Intent(context,MyGroupDetail::class.java)
        intent.putExtra("groupName",name)
        intent.putExtra("groupMembers", community)
        intent.putExtra("groupID", groupID)
        startActivity(intent)
    }

    override fun OnDeleteClicked(id: String) {
        db.collection("Groups")
            .document(id)
            .delete()
            .addOnSuccessListener {
                showToast("Group is deleted!")
                updateUserGroupCount()
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }
    }

    override fun OnMessageClicked(id: String, name: String) {
        val intent = Intent(context,ChatScreenActivity::class.java)
        intent.putExtra("ChatID","${id}")
        intent.putExtra("chatName","${name}")
        intent.putExtra("SenderID","9dpmQHz0LSOtc8MdXvGiHRk2Bsj1")
        intent.putExtra("chatType","many")
        startActivity(intent)


    }

    override fun onEditClicked(id: String, name: String) {
        showAlertChangeGroupName(id,name)
    }

    private fun showAlertChangeGroupName(id: String, name: String) {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = EditNameCustomAlertBinding.inflate(LayoutInflater.from(requireContext()))
        dialogBinding.groupName.setText(name)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialogBinding.UpdateTV.setOnClickListener {
            val groupName = dialogBinding.groupName.text.toString()
            changeGroupName(id,groupName)
            dialog.dismiss()
        }
        dialogBinding.CancelTV.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun changeGroupName(id: String, groupName: String) {

        db.collection("Groups")
            .document(id)
            .update("name","${groupName}")
            .addOnSuccessListener {
                showToast("Group Name Changed!")
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }
    }

    private fun updateUserGroupCount() {
        FirebaseFirestore.getInstance().collection("Users")
            .document(Constants.currentUser.id)
            .update("groupCount", FieldValue.increment(-1))
            .addOnSuccessListener {
                Constants.currentUser.groupCount--
            }
            .addOnFailureListener { exception->
                showAlert(exception.message)
            }
    }
}