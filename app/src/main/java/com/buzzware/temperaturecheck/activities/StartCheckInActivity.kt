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
import com.buzzware.temperaturecheck.model.QuestionModel
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
    private lateinit var questionAdapter : QuestionAdapter
    private var user_mode : Int = 0
    private var adapterQuestionArray : ArrayList<QuestionModel> = ArrayList()
    private val calendar = Calendar.getInstance()
    private var pastDaysDate : String = ""
    private var periodOfDay : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setStatusBarColor(R.color.cyan_color_20)


        pastDaysDate = intent.getStringExtra("missDate") ?: ""

        Log.d("LOGGER", "Update previous Record: $pastDaysDate")
        if (pastDaysDate.isNotEmpty())
        {
            Constants.answerList.clear()
            binding.titleTV.text = intent.getStringExtra("missDate")
            adapterQuestionArray.add(QuestionModel(0,"How did you sleep?", "", UUID.randomUUID().toString()))
            adapterQuestionArray.add(QuestionModel(1,"How do you feel about yourself today?", "", UUID.randomUUID().toString()))
            adapterQuestionArray.add(QuestionModel(2,"Are you looking forward to today?", "", UUID.randomUUID().toString()))
            adapterQuestionArray.add(QuestionModel(3,"How's your day going?", "", UUID.randomUUID().toString()))
            adapterQuestionArray.add(QuestionModel(4,"How did you feel about your day", "", UUID.randomUUID().toString()))

            setView()
        }else{
            getCurrentTime()
        }

        setListener()

    }

    private fun getCurrentTime() {
        mDialog.show()
        adapterQuestionArray.clear()
        val currentTime = calendar.get(Calendar.HOUR_OF_DAY)

        val checkQuestionArray: List<String>

        when (currentTime) {
            in 0..19 -> {
                binding.titleTV.text = "Morning Questions"
                periodOfDay = "Morning"
                checkQuestionArray = listOf("How did you sleep?", "How do you feel about yourself today?", "Are you looking forward to today?")
            }
            in 20..22 -> {
                binding.titleTV.text = "Afternoon Questions"
                periodOfDay = "Afternoon"
                checkQuestionArray = listOf("How's your day going?")
            }
            else -> {
                binding.titleTV.text = "Evening Questions"
                periodOfDay = "Evening"
                checkQuestionArray = listOf("How did you feel about your day?")
            }
        }

        var completedQueries = 0
        val totalQueries = checkQuestionArray.size

        Log.d("LOGGER", "checkQustion Size: ${checkQuestionArray.size}")
        checkQuestionArray.forEachIndexed { index, question ->
            db.collection("UserQuestions")
                .whereEqualTo("question", question)
                .whereEqualTo("userId", Constants.currentUser.id)
                .get().addOnSuccessListener { result ->
                    Log.d("LOGGER", "Result Size: ${result.size()}")

                    var isSameDayFound = false // Track if a same-day record exists

                    result.forEach { doc ->
                        val model = doc.toObject(UserQuestionModel::class.java)
                        Log.d("LOGGER", "Doc ID: ${doc.id}")
                        if (isSameDay(model.date.toLong(), System.currentTimeMillis())) {
                            Log.d("LOGGER", "is Same Day Added")
                            adapterQuestionArray.add(QuestionModel(index, question, model.answer, model.id))
                            isSameDayFound = true
                        }
                    }

                    if (!isSameDayFound) {
                        Log.d("LOGGER", "No same day record found, adding new one")
                        adapterQuestionArray.add(QuestionModel(index, question, "", UUID.randomUUID().toString()))
                    }

                    completedQueries++
                    if (completedQueries == totalQueries) {
                        setView()
                    }
                }.addOnFailureListener {
                    adapterQuestionArray.add(QuestionModel(index, question, "", UUID.randomUUID().toString()))
                    completedQueries++
                    if (completedQueries == totalQueries) {
                        setView()
                    }
                }
        }
    }

    private fun setView() {
        Log.d("LOGGER", "adpater size: ${adapterQuestionArray.size}")
        if (mDialog.isShowing) mDialog.dismiss()
        binding.questionVP.isUserInputEnabled = false
        questionAdapter = QuestionAdapter(this, adapterQuestionArray, this)
        binding.questionVP.adapter = questionAdapter
        questionAdapter.notifyDataSetChanged() // Ensure UI updates
        updateCurrentPosition(binding.questionVP.currentItem + 1, adapterQuestionArray.size)

    }

    private fun setListener() {

        binding.backIV.setOnClickListener { onBackPressed() }

        binding.nextLayout.setOnClickListener {
            val currentItem = binding.questionVP.currentItem

            if (Constants.answerList[currentItem]?.selectedAnswer.isNullOrEmpty()) {
                showToast("Please select an answer")
                return@setOnClickListener
            }

            if (currentItem < adapterQuestionArray.size - 1) {
                binding.questionVP.currentItem = currentItem + 1
                user_mode += questionAdapter.mode
                updateCurrentPosition(binding.questionVP.currentItem + 1, adapterQuestionArray.size)

                binding.nextTV.text = if (binding.questionVP.currentItem == adapterQuestionArray.size - 1) "Complete" else "Next"
            } else {
                user_mode += questionAdapter.mode
                if (pastDaysDate.isNotEmpty()) {
                    updatePreviousRecord(pastDaysDate)
                    Log.d("LOGGER", "Update previous Record")
                } else {
                    Log.d("LOGGER", "Update current record")
                    addToDaysQuestionsAns()
                }
            }
        }

        binding.backLayout.setOnClickListener {
            val currentItem = binding.questionVP.currentItem
            if (currentItem > 0) {
                binding.questionVP.currentItem = currentItem - 1
                updateCurrentPosition(binding.questionVP.currentItem + 1, adapterQuestionArray.size)

                if (binding.questionVP.currentItem < adapterQuestionArray.size - 1) {
                    binding.nextTV.text = "Next"
                }
            } else {
                // User is already on the first question
                showToast("You are already on the first question.")
            }
        }

    }

    private fun addToDaysQuestionsAns() {
        var count = 0
        Constants.answerList.forEach { (key, value) ->

            val model = UserQuestionModel(value.selectedAnswer, System.currentTimeMillis(), getCurrentDate(), value.currentId , value.question, key.toLong(), periodOfDay, Constants.currentUser.id)

            db.collection("UserQuestions").document(value.currentId)
                .set(model).addOnSuccessListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("userModel", Constants.currentUser)
                        intent.putParcelableArrayListExtra("questions_list", Constants.currentUserQuestion)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",adapterQuestionArray.size)
                        startActivity(intent)
                        overridePendingTransition(fadeIn, fadeOut)
                    }

                }.addOnFailureListener {
                    count++
                    if (count == Constants.answerList.size){
                        val intent = Intent(this, MyTempResultActivity::class.java)
                        intent.putExtra("userModel", Constants.currentUser)
                        intent.putParcelableArrayListExtra("questions_list", Constants.currentUserQuestion)
                        intent.putExtra("User_Mode",user_mode)
                        intent.putExtra("total",adapterQuestionArray.size)
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

    override fun onAnswerClicked() {
        val currentItem = binding.questionVP.currentItem
        if (currentItem < adapterQuestionArray.size - 1) {
            binding.questionVP.currentItem = currentItem + 1
            user_mode += questionAdapter.mode
            updateCurrentPosition(binding.questionVP.currentItem + 1, adapterQuestionArray.size)

            binding.nextTV.text = if (binding.questionVP.currentItem == adapterQuestionArray.size - 1) "Complete" else "Next"
        }
    }

    private fun updatePreviousRecord(pastDaysDate: String) {
        var count = 0

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(pastDaysDate)

        val timestamp = date?.time ?: 0L

        Constants.answerList.forEach { (index, value) ->
            val model = UserQuestionModel(
                value.selectedAnswer,
                timestamp,
                pastDaysDate,
                value.currentId,
                value.question,
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
        intent.putExtra("userModel", Constants.currentUser)
        intent.putParcelableArrayListExtra("questions_list", Constants.currentUserQuestion)
        intent.putExtra("User_Mode", user_mode)
        intent.putExtra("total", adapterQuestionArray.size)
        startActivity(intent)
        overridePendingTransition(fadeIn, fadeOut)
    }

    override fun onResume() {
        super.onResume()
        Constants.answerList.clear()
    }

}