package com.buzzware.temperaturecheck.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.buzzware.temperaturecheck.R

/*import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore*/

open class BaseActivity : AppCompatActivity() {

    //lateinit var mAuth: FirebaseAuth
    //lateinit var db: FirebaseFirestore
    lateinit var mDialog: ProgressDialog

    private var errorDialog: AlertDialog? = null

    val fadeIn = androidx.appcompat.R.anim.abc_fade_in
    val fadeOut = androidx.appcompat.R.anim.abc_fade_out

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You can add common functionality here that you want to execute in all activities

        //FirebaseApp.initializeApp(this)
        mDialog = ProgressDialog(this)
        mDialog.setMessage("Please wait...")
        mDialog.setCancelable(false)

        //db = FirebaseFirestore.getInstance()
        //mAuth = FirebaseAuth.getInstance()


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

    fun setStatusBarColor(colorResId: Int) {
        window.statusBarColor = resources.getColor(colorResId, theme)
    }

}