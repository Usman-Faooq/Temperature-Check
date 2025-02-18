package com.buzzware.temperaturecheck.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.QuestionAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ActivityStartCheckInBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class StartCheckInActivity : BaseActivity() {

    private val binding : ActivityStartCheckInBinding by lazy {
        ActivityStartCheckInBinding.inflate(layoutInflater)
    }
    lateinit var questionAdapter : QuestionAdapter
    var user_mode : Int = 0
    private var questionList : ArrayList<String> = ArrayList()
    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)



        getCurrentTime()
        setView()
        setListener()

    }


    private fun getCurrentTime() {

        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)
        when (currentTime) {
            in 5..11 -> {
                questionList.addAll(listOf("How did you sleep?", "How do you feel about yourself today?", "Are you looking forward to today?"))
                binding.titleTV.text = "Morning Questions"
            }
            in 12..19 -> {
                questionList.add("How's your day going?")
                binding.titleTV.text = "Afternoon Questions"
            }
            else -> {
                questionList.add("How did you feel about your day?")
                binding.titleTV.text = "Evening Questions"
            }
        }
    }

    private fun setView() {

        binding.questionVP.isUserInputEnabled = false
        questionAdapter = QuestionAdapter(this, questionList)
        binding.questionVP.adapter = questionAdapter
        updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.nextLayout.setOnClickListener {
            if (binding.questionVP.currentItem < questionList.size - 1 && binding.nextTV.text.toString() != "Complete")
            {
                binding.questionVP.currentItem += 1
                user_mode += questionAdapter.mode
                updateCurrentPosition(binding.questionVP.currentItem + 1, questionList.size)
                if (binding.questionVP.currentItem == questionList.size - 1){
                    binding.nextTV.text = "Complete"
                }else{
                    binding.nextTV.text = "Next"
                }
                addDataToUserQuestion(questionAdapter.question,questionAdapter.feel,getCurrentDate(),
                    convertToTimestamp(getCurrentDate()),Constants.currentUser.id,binding.titleTV.text.toString()
                    ,UUID.randomUUID().toString(),questionAdapter.question_sequence)

            }else{
                //complete click
                user_mode += questionAdapter.mode
                addDataToUserQuestion(
                    questionAdapter.question, questionAdapter.feel, getCurrentDate(),
                    convertToTimestamp(getCurrentDate()), Constants.currentUser.id, binding.titleTV.text.toString()
                    ,UUID.randomUUID().toString(),questionAdapter.question_sequence)
                val intent = Intent(this, MyTempResultActivity::class.java)
                intent.putExtra("User_Mode",user_mode)
                intent.putExtra("total",questionList.size)
                startActivity(intent)
                overridePendingTransition(fadeIn, fadeOut)

            }
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

    private fun addDataToUserQuestion(
        question: String,
        feel: String,
        date: String,
        dateTimeStamp: String,
        user_id: String,
        title_question: String,
        question_id: String,
        questionSequence: Int
    ) {
        mDialog.show()

        val questionData = mapOf(
            "answer" to feel
            ,"date" to dateTimeStamp
            ,"date1" to date
            ,"id" to question_id
            ,"question" to question
            ,"today" to title_question
            , "seq" to questionSequence
            ,"userId" to user_id)
        db.collection("UserQuestions")
            .document(question_id)
            .set(questionData)
            .addOnSuccessListener {
                mDialog.dismiss()

            }
            .addOnFailureListener { exception ->
                mDialog.dismiss()
                showAlert(exception.message.toString())

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
}