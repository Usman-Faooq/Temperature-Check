package com.buzzware.temperaturecheck.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityMainBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import java.net.URL
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val TAG = "DynamicLinkHandler"

    private val permissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()
        checkAndRequestPermissions()

        handleDynamicLink(intent)

    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            //Toast.makeText(this, "All permissions are already granted", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                100
            )
        }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let {
            handleDynamicLink(it)
        }
    }

    private fun handleDynamicLink(intent: Intent) {
        Log.d(TAG, "Intent Data : ${intent.data.toString()}")

        val uri: Uri? = intent.data
        // checking if the uri is null or not.
        if (uri != null) {

            val userId = uri.getQueryParameter("userId").toString()
            val groupId = uri.getQueryParameter("groupId").toString()

            Log.d(TAG, "Extracted userId: $userId")
            Log.d(TAG, "Extracted groupId: $groupId")

            Constants.intentUserID = userId
            Constants.intentGroupID = groupId
            if (mAuth.currentUser != null)
            {
                checkforCurrentUser()
            }
        }else{
            if (mAuth.currentUser != null)
            {
                checkforCurrentUser()
            }
        }
    }

}
