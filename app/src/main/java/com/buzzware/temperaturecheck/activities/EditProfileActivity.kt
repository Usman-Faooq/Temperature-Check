package com.buzzware.temperaturecheck.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityEditProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditProfileActivity : BaseActivity() {

    private val binding : ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private var dateOfBirth : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setView()
        setListener()

    }

    private fun setView() {
        binding.usernameET.setText(Constants.currentUser.userName.toString())
        binding.UserEmailET.setText(Constants.currentUser.email.toString())
        binding.DOBET.setText(convertTimestampStringToDate(Constants.currentUser.dob.toString().takeIf { it != "0" } ?: ""))
        binding.PhoneET.setText(Constants.currentUser.phoneNumber.toString())
        binding.passwordET.setText(Constants.currentUser.password.toString())

        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(binding.profileImage)

    }


    private fun setListener(){

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.SaveButton.setOnClickListener {
            if(checkFields())
            {
                updateDataInFirebsaeAuthentication()
                mDialog.show()
            }
        }
        binding.DOBET.setOnClickListener {
            showMaterialDatePicker()
        }



    }

    private fun updateDataInFirebsaeAuthentication(){
        val username = binding.usernameET.text.toString()
        val useremail = binding.UserEmailET.text.toString()
        val dateofbirth = convertToTimestamp(binding.DOBET.text.toString())
        val userphone = binding.PhoneET.text.toString()
        val userpassword = binding.passwordET.text.toString()

        mAuth.currentUser!!.updateEmail(useremail)
            .addOnSuccessListener {
                Constants.currentUser.email = useremail
                mAuth.currentUser!!.updatePassword(userpassword)
                    .addOnSuccessListener{
                        updateDataInFirestore(username,useremail,dateofbirth,userphone,userpassword)
                        Constants.currentUser.password = userpassword

                    }
                    .addOnFailureListener { exception ->
                        showAlert(exception.message.toString())
                        mDialog.dismiss()
                    }
            }
            .addOnFailureListener {exception ->
                showAlert(exception.message.toString())

            }


    }

    private fun updateDataInFirestore(username: String, useremail: String, dateofbirth: Long, userphone: String, userpassword: String)
    {

        val fullname = username.split(" ")
        val firstName = fullname[0]
        val lastName = fullname.getOrNull(1) ?: ""

        val updatedData = mapOf(
            "userName" to username,
            "firstName" to firstName,
            "lastName" to lastName,
            "password" to userpassword,
            "dob" to dateofbirth,
            "phoneNumber" to userphone,
            "email" to useremail,
            )
        db.collection("Users")
            .document(Constants.currentUser.id)
            .update(updatedData)
            .addOnSuccessListener {
                showAlert("Data is Updated Successfully!")
                Constants.currentUser.userName = username
                Constants.currentUser.firstName = firstName
                Constants.currentUser.lastName = lastName
                Constants.currentUser.dob = dateofbirth
                Constants.currentUser.phoneNumber = userphone
                mDialog.dismiss()

            }.addOnFailureListener { exception ->
                showAlert(exception.message.toString())
                mDialog.dismiss()
            }


    }

    private fun checkFields(): Boolean {
        if (binding.usernameET.text.isEmpty())
        {
            binding.usernameET.setError("User name is required")
            binding.usernameET.requestFocus()

            return false
        }
        else if(binding.UserEmailET.text.isEmpty())
        {
            binding.UserEmailET.setError("User email is required")
            binding.UserEmailET.requestFocus()
            return false
        }
        else if(binding.DOBET.text.isEmpty())
        {
            binding.DOBET.setError("User Date of Birth is required")
            binding.DOBET.requestFocus()
            return false
        }
        else if(binding.PhoneET.text.isEmpty())
        {
            binding.PhoneET.setError("User Phone Number is required")
            binding.PhoneET.requestFocus()
            return false
        }
        else if(binding.passwordET.text.isEmpty())
        {
            binding.passwordET.setError("User password is required")
            binding.passwordET.requestFocus()
            return false
        }
        else
        {
            return true
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

    fun convertFromTimestamp(timestamp: String): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun convertTimestampStringToDate(timestampString: String): String {
        return try {
            val timestamp = timestampString.toLong() // Convert String to Long
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timestamp)) // Convert to date string
        } catch (e: NumberFormatException) {
            ""
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

}