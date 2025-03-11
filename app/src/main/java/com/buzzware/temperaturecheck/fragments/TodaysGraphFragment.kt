package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentTodaysGraphBinding
import com.buzzware.temperaturecheck.model.ChatResponse
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.buzzware.temperaturecheck.retrofit.AIController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TodaysGraphFragment(val res: Int, val selectedUser: UserModel, selectedQuestions: ArrayList<UserQuestionModel>) : BaseFragment() {

    private val binding : FragmentTodaysGraphBinding by lazy {
        FragmentTodaysGraphBinding.inflate(layoutInflater)
    }

    private var total : Int = 1
    private var color : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{

        setViews()
        hitApi()


        return binding.root
    }

    private fun setViews() {
        binding.nameTV.text = "${selectedUser.firstName} ${selectedUser.lastName}"
        Glide.with(this)
            .load(selectedUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.rootIV)

        when (res) {
            5 -> {
                color = "green"
                binding.personIV5.visibility = View.VISIBLE
                Glide.with(this)
                    .load(selectedUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV5)
            }
            4 -> {
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

    fun getTextToShare(): String {
        return binding.contentTV.text.toString()
    }
}