package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.ItemDesignQuestionLayoutBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.util.Calendar
import java.util.Date

class QuestionAdapter(val context: Context, private val list: ArrayList<String>, val listener : QuestionInterface) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var mode : Int = 0
    var feel : String = ""
    var question : String = ""
   // var questionId : String = ""
    var question_sequence : Int = 0

    interface QuestionInterface{
        fun onAnswerClicked(feel : String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignQuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.questionTV.text = list[position]
        question = holder.binding.questionTV.text.toString()
        question_sequence = position


        //Log.d("TAG12345","${question}")



        holder.binding.option01.setOnClickListener{
            setSelector(holder.binding.option01, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option05)
            mode = 1
            feel = "Very Happy"
            Constants.answerList[position] = "Very Happy"
        }

        holder.binding.option02.setOnClickListener{
            setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
            mode = 2
            feel = "Happy"
            Constants.answerList[position] = "Happy"
        }

        holder.binding.option03.setOnClickListener{
            setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
            mode = 3
            feel = "Uneasy"
            Constants.answerList[position] = "Uneasy"
        }

        holder.binding.option04.setOnClickListener{
            setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
            mode = 4
            feel = "Sad"
            Constants.answerList[position] = "Sad"
        }

        holder.binding.option05.setOnClickListener{
            setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)
            mode = 5
            feel = "Angry"
            Constants.answerList[position] = "Angry"
        }

        if (Constants.currentUserQuestion.isNotEmpty()){
            getQuestionData(holder, position)
        }

    }


    private fun getQuestionData(holder: ViewHolder, position: Int) {

        val it = Constants.currentUserQuestion[position]
        if (isSameDay(System.currentTimeMillis(),it.date.toLong()) )
        {
            val periodOfDay = it.today
            val modeOfUser = it.answer
          //  questionId = it.id
            val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            when (currentTime) {
                in 0..19 -> {
                    if(periodOfDay == "Morning" && isSameDay(System.currentTimeMillis(),it.date.toLong()) )
                    {
                        when (modeOfUser)
                        {
                            "Very Happy" -> setSelector(holder.binding.option01, holder.binding.option02,holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Happy" -> setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Uneasy" -> setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
                            "Sad" -> setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
                            "Angry" -> setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)
                        }
                        Constants.answerList[position] = modeOfUser

                    }
                }
                in 20..22 -> {
                    if(periodOfDay == "Afternoon" && isSameDay(System.currentTimeMillis(),it.date.toLong()) )
                    {
                        when (modeOfUser)
                        {
                            "Very Happy" -> setSelector(holder.binding.option01, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Happy" -> setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Uneasy" -> setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
                            "Sad" -> setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
                            "Angry" -> setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)

                        }
                        Constants.answerList[position] = modeOfUser
                    }
                }
                else -> {
                    if(periodOfDay == "Evening" && isSameDay(System.currentTimeMillis(),it.date.toLong()))
                    {
                        when (modeOfUser)
                        {
                            "Very Happy" -> setSelector(holder.binding.option01, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Happy" -> setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
                            "Uneasy" -> setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
                            "Sad" -> setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
                            "Angry" -> setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)

                        }
                        Constants.answerList[position] = modeOfUser
                    }

                }
            }
        }

    }


    inner class ViewHolder(val binding : ItemDesignQuestionLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private fun setSelector(selected: ImageView, img1: ImageView, img2: ImageView, img3: ImageView, img4: ImageView) {

        selected.setBackgroundResource(R.drawable.rounded_selector)
        img1.setBackgroundResource(R.drawable.rounded_with_gray_border)
        img2.setBackgroundResource(R.drawable.rounded_with_gray_border)
        img3.setBackgroundResource(R.drawable.rounded_with_gray_border)
        img4.setBackgroundResource(R.drawable.rounded_with_gray_border)

    }


    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

}
