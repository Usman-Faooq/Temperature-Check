package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.QuestionAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityStartCheckInBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class StartCheckInActivity : BaseActivity(), QuestionAdapter.QuestionInterface {

    private val binding : ActivityStartCheckInBinding by lazy {
        ActivityStartCheckInBinding.inflate(layoutInflater)
    }
    lateinit var questionAdapter : QuestionAdapter
    var user_mode : Int = 0
    private var questionList : ArrayList<String> = ArrayList()
    val calendar = Calendar.getInstance()
    var pastDaysDate : String = ""
    var periodOfDay : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)


        pastDaysDate = intent.getStringExtra("missDate") ?: ""


        if (pastDaysDate.isNotEmpty())
        {
            Constants.answerList.clear()
            binding.titleTV.text = intent.getStringExtra("missDate")
            questionList.addAll(listOf("How did you sleep?", "How do you feel about yourself today?",
                "Are you looking forward to today?"
            ,"How's your day going?"
            ,"How did you feel about your day"))
        }else{
            getCurrentTime()
        }

        setView()
        setListener()

    }

    private fun updatePrevousRecord(pastDaysDate: String) {
        var count = 0
        Constants.answerList.values.forEachIndexed { index, s ->

            val model = UserQuestionModel(s, convertToTimestamp(pastDaysDate), pastDaysDate, UUID.randomUUID().toString(), questionList[index], index.toLong(), binding.titleTV.text.toString(), Constants.currentUser.id)

            db.collection("UserQuestions").document(model.id)
                .set(model).addOnSuccessListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",questionList.size)
                        startActivity(intent)
                        overridePendingTransition(fadeIn, fadeOut)
                    }

                }.addOnFailureListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",questionList.size)
                        startActivity(intent)
                        overridePendingTransition(fadeIn, fadeOut)
                    }
                }
        }

    }


    private fun getCurrentTime() {

        questionList.clear()
        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)
        when (currentTime) {
            in 0..19 -> {
                questionList.add("How did you sleep?")
                questionList.add("How do you feel about yourself today?")
                questionList.add("Are you looking forward to today?")
                binding.titleTV.text = "Morning Questions"
                periodOfDay = "Morning"
            }
            in 20 ..22 -> {
                questionList.add("How's your day going?")
                binding.titleTV.text = "Afternoon Questions"
                periodOfDay = "Afternoon"
            }
            else -> {
                questionList.add("How did you feel about your day?")
                binding.titleTV.text = "Evening Questions"
                periodOfDay = "Evening"
            }
        }
    }

    private fun setView() {

        binding.questionVP.isUserInputEnabled = false
        questionAdapter = QuestionAdapter(this, questionList, this)
        binding.questionVP.adapter = questionAdapter
        updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.nextLayout.setOnClickListener {
            val currentItem = binding.questionVP.currentItem

            Log.d("TAG12345", "Before Increment: ${Constants.answerList}")

            if (Constants.answerList[currentItem].isNullOrEmpty()) {
                showToast("Please select an answer")
                return@setOnClickListener
            }

            if (currentItem < questionList.size - 1) {
                binding.questionVP.currentItem = currentItem + 1
                user_mode += questionAdapter.mode
                updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

                binding.nextTV.text = if (binding.questionVP.currentItem == questionList.size - 1) "Complete" else "Next"
            } else {
                user_mode += questionAdapter.mode
                if (pastDaysDate.isNotEmpty()) {
                    updatePreviousRecord(pastDaysDate)
                } else {
                    addToDaysQuestionsAns()
                }
            }

            Log.d("TAG12345", "After Increment: ${Constants.answerList}")
        }

        binding.backLayout.setOnClickListener {
            val currentItem = binding.questionVP.currentItem
            if (currentItem > 0) {
                binding.questionVP.currentItem = currentItem - 1
                updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

                if (binding.questionVP.currentItem < questionList.size - 1) {
                    binding.nextTV.text = "Next"
                }
            } else {
                // User is already on the first question
                showToast("You are already on the first question.")
            }
        }

    }

    private fun addToDaysQuestionsAns()
    {
        var count = 0
        var id : String = ""
        Constants.answerList.forEach { (key, value) ->
//            if (questionAdapter.questionId.isNotEmpty())
//            {
//                id = questionAdapter.questionId.toString()
//            }else{
//
//                id = UUID.randomUUID().toString()
//            }
//            questionAdapter.questionId

            val model = UserQuestionModel(value, System.currentTimeMillis().toString(), getCurrentDate(), UUID.randomUUID().toString() , questionList[key], key.toLong(), periodOfDay, Constants.currentUser.id)

            db.collection("UserQuestions").document(model.id)
                .set(model).addOnSuccessListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",questionList.size)
                        startActivity(intent)
                        overridePendingTransition(fadeIn, fadeOut)
                    }

                }.addOnFailureListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",questionList.size)
                        startActivity(intent)
                        overridePendingTransition(fadeIn, fadeOut)
                    }
                }
        }
    }



    private fun updateCurrentPosition(current: Int, total: Int) {
        binding.currentPositionTV.text = current.toString()
        binding.totalQuestionTV.text = " / $total"
    }

    fun convertToTimestamp(dateString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateString)
            (date?.time ?: 0L).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "0"
        }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(fadeIn, fadeOut)
    }

    override fun onAnswerClicked(feel: String) {

    }

    private fun updatePreviousRecord(pastDaysDate: String) {
        var count = 0
        Constants.answerList.forEach { (index, value) ->
            val model = UserQuestionModel(
                value,
                convertToTimestamp(pastDaysDate),
                pastDaysDate,
                UUID.randomUUID().toString(),
                questionList[index],
                index.toLong(),
                binding.titleTV.text.toString(),
                Constants.currentUser.id
            )

            db.collection("UserQuestions").document(model.id)
                .set(model)
                .addOnSuccessListener {
                    count++
                    if (count == Constants.answerList.size) {
                        navigateToResult()
                    }
                }
                .addOnFailureListener {
                    count++
                    if (count == Constants.answerList.size) {
                        navigateToResult()
                    }
                }
        }
    }

    private fun navigateToResult() {
        val intent = Intent(this, MyTempResultActivity::class.java)
        intent.putExtra("User_Mode", user_mode)
        intent.putExtra("total", questionList.size)
        startActivity(intent)
        overridePendingTransition(fadeIn, fadeOut)
    }

    override fun onResume() {
        super.onResume()
        Constants.answerList.clear()
    }

}