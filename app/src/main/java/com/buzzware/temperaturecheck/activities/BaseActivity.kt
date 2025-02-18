package com.buzzware.temperaturecheck.activities

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
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ShowCustomeAlertBinding

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class BaseActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var mDialog: ProgressDialog

    private var errorDialog: AlertDialog? = null

    val fadeIn = androidx.appcompat.R.anim.abc_fade_in
    val fadeOut = androidx.appcompat.R.anim.abc_fade_out

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        mDialog = ProgressDialog(this)
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
        errorDialog = AlertDialog.Builder(this)
            .setMessage(msg)
            .setTitle(title)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        errorDialog!!.show()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    fun showAlert(message: String?) {

        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = ShowCustomeAlertBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        dialogBinding.messageTV.text = message.toString()

        dialogBinding.yesTV.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }


    fun setStatusBarColor(colorResId: Int) {
        window.statusBarColor = resources.getColor(colorResId, theme)
    }

}