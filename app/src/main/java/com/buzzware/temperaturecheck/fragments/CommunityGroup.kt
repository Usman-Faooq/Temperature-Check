package com.buzzware.temperaturecheck.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.activities.ChatScreenActivity
import com.buzzware.temperaturecheck.activities.MyGroupDetail
import com.buzzware.temperaturecheck.adapters.CommunityGroupAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentCommunityGroupBinding
import com.buzzware.temperaturecheck.databinding.LeaveGroupCustomAlertBinding
import com.buzzware.temperaturecheck.model.GroupModel
import com.google.firebase.firestore.FieldValue


class CommunityGroup : BaseFragment(), CommunityGroupAdapter.OnClicked{

    private val binding : FragmentCommunityGroupBinding by lazy {
        FragmentCommunityGroupBinding.inflate(layoutInflater)
    }
    private lateinit var context: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{



        getCommunityGroupData()



        return binding.root
    }

    private fun getCommunityGroupData() {

        db.collection("Groups")
            .whereEqualTo("comunity.${Constants.currentUser.id}","accepted")
            .addSnapshotListener { value, error ->
                if (error?.message != null)
                {
                    showAlert(error.message)
                    return@addSnapshotListener
                }

                val model = value?.toObjects(GroupModel::class.java) as ArrayList<GroupModel>

                val linearLayoutManager = LinearLayoutManager(context)
                binding.communityGroupRecycler.layoutManager = linearLayoutManager
                binding.communityGroupRecycler.adapter = CommunityGroupAdapter(context,model,this)

                
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun OnItemClicked(id: String, name: String, comunity: HashMap<String, String>) {
        val intent = Intent(context,MyGroupDetail::class.java)
        intent.putExtra("groupName",name)
        intent.putExtra("groupMembers", comunity)
        intent.putExtra("groupID", id)
        startActivity(intent)
    }

    override fun OnLeaveClicked(name: String, groupID: String) {
        showAlertCustome(name,groupID)
    }

    override fun OnMessageIconClicked(name: String, id: String) {
        val intent = Intent(context,ChatScreenActivity::class.java)
        intent.putExtra("ChatID","${id}")
        intent.putExtra("chatName","${name}")
        intent.putExtra("SenderID","9dpmQHz0LSOtc8MdXvGiHRk2Bsj1")
        intent.putExtra("chatType","many")
        startActivity(intent)

    }


    fun showAlertCustome(name: String, groupID: String) {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = LeaveGroupCustomAlertBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialogBinding.messageTV.text = "Are you sure you want to leave ${name} group?"

        dialogBinding.yesTV.setOnClickListener {
            leftGroup(groupID)
            dialog.dismiss()}
        dialogBinding.NoTV.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun leftGroup(groupID: String) {

        val fieldValue = hashMapOf<String,Any>(
            "comunity.${Constants.currentUser.id}" to FieldValue.delete()
        )

        db.collection("Groups")
            .document(groupID)
            .update(fieldValue)
            .addOnSuccessListener {
                showToast("You have left the group!")
            }
            .addOnFailureListener { excepton ->
                showAlert(excepton.message)
            }


    }
}