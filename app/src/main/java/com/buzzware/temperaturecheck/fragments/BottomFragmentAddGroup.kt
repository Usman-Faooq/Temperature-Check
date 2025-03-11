package com.buzzware.temperaturecheck.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentBottomAddGroupBinding
import com.buzzware.temperaturecheck.databinding.ShowCustomeAlertBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class BottomFragmentAddGroup : BottomSheetDialogFragment() {

    private val binding : FragmentBottomAddGroupBinding by lazy {
        FragmentBottomAddGroupBinding.inflate(layoutInflater)
    }

    private lateinit var fragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        setListener()


        return binding.root
    }

    private fun setListener() {

        binding.backIV.setOnClickListener {  dismiss()   }


        binding.addTeam.setOnClickListener {
            if (binding.teamName.text.isEmpty())
            {
                showAlert("Please Enter Your Group Name")
                return@setOnClickListener
            }
            addNewTeam(binding.teamName.text.toString(),UUID.randomUUID().toString())
        }


    }

    private fun addNewTeam(teamName: String, UID: String) {
        val data : Map<String, Any> = mapOf(
            "date" to System.currentTimeMillis(),
            "groupCount" to 5,
            "id" to UID,
            "lastcheckin" to 0,
            "name" to teamName,
            "userId" to "${Constants.currentUser.id}")

        FirebaseFirestore.getInstance().collection("Groups")
            .document(UID)
            .set(data)
            .addOnSuccessListener {
                showAlert("You Have successfully created ${teamName} group!")
                updateUserGroupCount()
                dismiss()
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message)
            }



    }

    private fun updateUserGroupCount() {
        FirebaseFirestore.getInstance().collection("Users")
            .document(Constants.currentUser.id)
            .update("groupCount",FieldValue.increment(1))
            .addOnSuccessListener {
                Constants.currentUser.groupCount++
            }
            .addOnFailureListener { exception->
                showAlert(exception.message)
            }
    }


    fun showAlert(message: String?) {

        val dialog = Dialog(fragmentContext)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = ShowCustomeAlertBinding.inflate(LayoutInflater.from(fragmentContext))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialogBinding.messageTV.text = message.toString()
        dialogBinding.yesTV.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

}