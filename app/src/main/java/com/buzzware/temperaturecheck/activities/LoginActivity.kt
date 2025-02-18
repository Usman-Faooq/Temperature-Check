package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : BaseActivity() {

    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }



    private lateinit var googleSignInClient: GoogleSignInClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)


        googleSignIn()
        setListener()

    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.signInTV.setOnClickListener {
            signInUser()
        }

        binding.dontHaveAccount.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            overridePendingTransition(fadeIn,fadeOut)
        }


        binding.GoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

    }

    private fun signInUser() {

        val email = binding.emailET.text.toString()
        val password = binding.passwordET.text.toString()
        if (email.isEmpty())
        {
            binding.emailET.setError("User email is required")
            binding.emailET.requestFocus()
        }
        else if(password.isEmpty()){
            binding.passwordET.setError("User password is required")
            binding.passwordET.requestFocus()
        }
        else
        {
            mDialog.show()
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                if (task.isSuccessful)
                {
                    mDialog.dismiss()
                    startActivity(Intent(this, IndividualHomeActivity::class.java))
                    overridePendingTransition(fadeIn, fadeOut)

                } else{
                    mDialog.dismiss()
                    showAlert(task.exception?.message.toString())

                }
            }

        }
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 9001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try
            {
                mDialog.show()

                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        val newuser: Boolean = authTask.result.getAdditionalUserInfo()!!.isNewUser()
                        if (newuser)
                        {
                            getFCMtoken(authTask.result.user?.uid,account)


                        }
                        else
                        {
                            val intent = Intent(this,IndividualHomeActivity::class.java)
                            intent.putExtra("User_ID",authTask.result.user?.uid)
                            startActivity(intent)
                            mDialog.dismiss()
                        }
                    }

            }catch(e: ApiException){
                showToast(e.toString())
                mDialog.dismiss()

            }
        }
    }

    private fun getFCMtoken(UserID: String?, account: GoogleSignInAccount?) {

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            uploadUserDataInFirestore(UserID,account, it.toString())
        }

    }

    private fun uploadUserDataInFirestore(
        UserID: String?,
        account: GoogleSignInAccount?,
        token: String
    ){

        val userData = mapOf(
            "id" to UserID,
            "firstName" to account?.givenName,
            "lastName" to account?.familyName,
            "userName" to "${account?.givenName} ${account?.familyName}",
            "email" to account?.email,
            "image" to account?.photoUrl,
            "deviceType" to "Android",
            "userRole" to "user",
            "isApproved" to false,
            "isOnline" to true,
            "isSubsCribed" to true,
            "token" to token,
            "type" to Constants.selectedType)


        db.collection("Users").document(UserID!!)
            .set(userData)
            .addOnSuccessListener {
                mDialog.dismiss()
                val intent = Intent(this,IndividualHomeActivity::class.java)
                intent.putExtra("User_ID",UserID)
                startActivity(intent)
            }
            .addOnFailureListener {exception ->
                showAlert(exception.message.toString())
                mDialog.dismiss()

            }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}