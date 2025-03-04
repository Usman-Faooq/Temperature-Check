package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityMainBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (mAuth.currentUser != null)
        {
            checkforCurrentUser()
        }
        setListener()


    }


    private fun checkforCurrentUser() {

            mDialog.show()
            db.collection("Users")
                .document(mAuth.currentUser!!.uid)
                .get()
                .addOnSuccessListener{ document ->
                    if (document.exists())
                    {
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { it ->
                            db.collection("Users").document(mAuth.currentUser!!.uid).update("token", it.toString())
                        }
                        Constants.currentUser = document.toObject(UserModel::class.java)!!
                        startActivity(Intent(this,IndividualHomeActivity::class.java))
                        finish()
                        mDialog.dismiss()
                    }
                    else
                    {
                        mDialog.dismiss()
                    }

                }
                .addOnFailureListener{ exception ->
                    mDialog.dismiss()
                    showAlert(exception.message.toString())
                }
    }


    private fun setListener() {

        binding.signInTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.signUpTV.setOnClickListener {
            startActivity(Intent(this, NewSelectorActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

    }
}