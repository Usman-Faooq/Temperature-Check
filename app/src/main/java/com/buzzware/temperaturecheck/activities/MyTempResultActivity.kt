package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityMyTempResultBinding
import com.buzzware.temperaturecheck.model.ChatResponse
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.buzzware.temperaturecheck.retrofit.AIController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyTempResultActivity : BaseActivity() {

    private val binding : ActivityMyTempResultBinding by lazy {
        ActivityMyTempResultBinding.inflate(layoutInflater)
    }
    private var user_mode : Int? = 0
    private var total : Int = 1
    private var color : String = ""
    private var res : Int? = 0

    private var selectedUser: UserModel = UserModel()
    private var selectedQuestions: ArrayList<UserQuestionModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        selectedUser = intent.getParcelableExtra("userModel")?: UserModel()
        selectedQuestions = intent.getParcelableArrayListExtra("questions_list")?: arrayListOf()

        user_mode = intent.getIntExtra("User_Mode",0)
        total = intent.getIntExtra("total",0)

        Log.d("LOGGER", "selecteQestion: ${selectedQuestions.size} , total: $total")
        setView()
        setListener()

    }

    private fun setView() {
        res = user_mode?.div(total)

        Log.d("LOGGER", "Res: $res")
        when (res) {
            5 -> {
                //color = "very happy"
                color = "green"
                binding.personIV5.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV5)
            }
            4 -> {
                //color = "happy"
                color = "light green"
                binding.personIV4.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV4)

            }
            3 -> {
                color = "yellow"
                binding.personIV3.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV3)

            }
            2 -> {
                color = "light red"
                binding.personIV2.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV2)

            }
            1 -> {
                color = "red"
                binding.personIV1.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV1)
            }
        }

        hitApi()
    }

    private fun hitApi() {
        mDialog.show()
        Log.d("LOGGER", "Total: $total, Color: $color")
        val query = "We are $total group members and our average current temperature level is $color. " +
                "So [red is angry, light red is sad, yellow is uneasy, light green is happy, green is very happy]. " +
                "What can we do today according to this color?"

        val requestBody = mapOf(
            "model" to "gpt-3.5-turbo-0125",
            "messages" to listOf(mapOf("role" to "user", "content" to query)),
            "temperature" to 0,
            "max_tokens" to 1000,
            "top_p" to 1,
            "frequency_penalty" to 0.0,
            "presence_penalty" to 0.0
        )

        AIController.instance.getChatResponse(requestBody)
            .enqueue(object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    mDialog.dismiss()
                    if (response.isSuccessful) {
                        binding.contentTV.text = response.body()?.choices?.firstOrNull()?.message?.content.toString()
                    }else{
                        showAlert("Message: $response")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    mDialog.dismiss()
                    showAlert("Message: ${t.message}")
                }
            })
    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.findTV.setOnClickListener {
            startActivity(Intent(this, FindTherapistActivity::class.java))
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.DetailedAnalytics.setOnClickListener {
            val intent = Intent(this,TempResultActivity::class.java)
            //val intent = Intent(this,CommunityGraphActivity::class.java)
            intent.putExtra("userModel", selectedUser)
            intent.putParcelableArrayListExtra("questions_list", selectedQuestions)
            intent.putExtra("ResultOfMode",res)
            startActivity(intent)
            overridePendingTransition(fadeIn, fadeOut)
        }

        binding.shareIV.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, binding.contentTV.text.toString())
            }
            startActivity(Intent.createChooser(intent, "Share via"))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,IndividualHomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(fadeIn, fadeOut)
    }
}