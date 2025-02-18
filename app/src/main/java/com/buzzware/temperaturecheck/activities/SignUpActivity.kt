package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivitySignUpBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpActivity : BaseActivity() {

    private val binding : ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private var dateOfBirth : Long = 0
    var uid : String = ""
    var Username : String = ""
    var Useremail : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)

        setListener()
        checkForIntents()

    }

    private fun checkForIntents(){
        uid = intent.getStringExtra("uid") ?: ""
        Username = intent.getStringExtra("name") ?: ""
        Useremail = intent.getStringExtra("email") ?: ""

        if (uid.isNotEmpty() && Useremail.isNotEmpty() && Username.isNotEmpty())
        {

            binding.fullNameET.setText(Username)
            binding.EmailET.setText(Useremail)


        }
    }

    private fun setListener() {

        binding.aleadyAccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(fadeIn,fadeOut)
        }

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.signUpTV.setOnClickListener {
            if (Constants.selectedType != "individual" && checkFields())
            {
                startActivity(Intent(this, PaymentPlanActivity::class.java))
                overridePendingTransition(fadeIn, fadeOut)

            }else if(checkFields())
            {
                uploadUserDataInFirebaseAuthentication()
            }
        }

        binding.PasswordET.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_NEXT) {
                showMaterialDatePicker()
                binding.PasswordET.clearFocus()
                true
            } else {
                false
            }
        }

        binding.DOBET.setOnClickListener {
            showMaterialDatePicker()
        }


    }


    private fun checkFields(): Boolean{

        if (binding.fullNameET.text.isEmpty()) {
            binding.fullNameET.setError("User name can't be empty")
            binding.fullNameET.requestFocus()
            return false
        }
        else if(binding.EmailET.text.isEmpty())
        {
            binding.EmailET.setError("User email can't be empty")
            binding.EmailET.requestFocus()
            return false
        }
        else if(binding.DOBET.text.isEmpty())
        {
            binding.DOBET.setError("User date of birth can't be empty")
            binding.DOBET.requestFocus()
            return false
        }
        else if(binding.PhoneET.text.isEmpty())
        {
            binding.PhoneET.setError("User phone number can't be empty")
            binding.PhoneET.requestFocus()
            return false
        }
        else if (binding.PasswordET.text.isEmpty())
        {
            binding.PasswordET.setError("User password can't be empty")
            binding.PasswordET.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    private fun uploadUserDataInFirebaseAuthentication() {
        val fullName = binding.fullNameET.text.toString()
        val userEmail = binding.EmailET.text.toString()
        val userDOB = binding.DOBET.text.toString()
        val userPhone = binding.PhoneET.text.toString()
        val userPassword = binding.PasswordET.text.toString()

        mDialog.show()


        val names = fullName.split(" ")
        val firstName = names[0]
        val lastName = names.getOrNull(1) ?: ""

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnSuccessListener{ task ->

             val userID = task.user?.uid.toString()
            getFCMtoken(userID,fullName,userEmail,userDOB,userPhone,userPassword,firstName,lastName)
        }.addOnFailureListener {error ->
            showAlert(error.message)
            mDialog.dismiss()
        }
    }



    private fun getFCMtoken(
        userID: String,
        fullName: String,
        userEmail: String,
        userDOB: String,
        userPhone: String,
        userPassword: String,
        firstName: String,
        lastName: String
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful)
            {
                uploadUserDataInFirestore(userID,fullName,userEmail,userDOB,userPhone,userPassword,firstName,lastName,task.result)

            }else
            {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                mDialog.dismiss()
            }
        }
    }


    private fun uploadUserDataInFirestore(
        userID: String,
        fullName: String,
        userEmail: String,
        userDOB: String,
        userPhone: String,
        userPassword: String,
        firstName: String,
        lastName: String,
        token: String
    ) {

        val userData = mapOf(
            "id" to userID,
            "firstName" to firstName,
            "lastName" to lastName,
            "userName" to fullName,
            "email" to userEmail,
            "dob" to convertToTimestamp(userDOB),
            "phoneNumber" to userPhone,
            "password" to userPassword,
            "deviceType" to "Android",
            "userRole" to "user",
            "isApproved" to false,
            "isOnline" to true,
            "isSubsCribed" to true,
            "token" to token,
            "type" to Constants.selectedType)

        db.collection("Users").document(userID)
            .set(userData)
            .addOnSuccessListener {

                showAlert("Your Account is Created Successfully! \n" +
                        "You can now login")
                mDialog.dismiss()

            }
            .addOnFailureListener {error ->
                showAlert(error.message.toString())
                mDialog.dismiss()

            }




    }





    private fun showMaterialDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selection ->
            dateOfBirth = selection ?: 0L
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateOfBirth))
            binding.DOBET.text = formattedDate
        }
    }


    fun convertToTimestamp(dateString: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.parse(dateString)?.time ?: 0L
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }
}