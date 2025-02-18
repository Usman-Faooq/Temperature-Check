package com.buzzware.temperaturecheck.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ShowCustomeAlertBinding

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class BaseFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var mDialog: ProgressDialog

    private var errorDialog: AlertDialog? = null

    val fadeIn = androidx.appcompat.R.anim.abc_fade_in
    val fadeOut = androidx.appcompat.R.anim.abc_fade_out

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You can add common functionality here that you want to execute in all activities

        FirebaseApp.initializeApp(requireContext())
        mDialog = ProgressDialog(requireActivity())
        mDialog.setMessage("Please wait...")
        mDialog.setCancelable(false)

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()


    }

    // You can add other common methods or properties here

    fun showError(title: String, msg: String) {
        if (errorDialog != null && errorDialog!!.isShowing) {
            return
        }
        errorDialog = AlertDialog.Builder(requireActivity())
            .setMessage(msg)
            .setTitle(title)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        errorDialog!!.show()
    }


    fun showAlert(message: String?) {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = ShowCustomeAlertBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)
        dialogBinding.messageTV.text = message.toString()
        dialogBinding.yesTV.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }



}