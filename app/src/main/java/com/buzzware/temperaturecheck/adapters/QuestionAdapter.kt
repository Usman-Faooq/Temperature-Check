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
import com.buzzware.temperaturecheck.model.QuestionModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.util.Calendar
import java.util.Date

class QuestionAdapter(val context: Context, private val list: ArrayList<QuestionModel>, val listener : QuestionInterface) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var mode : Int = 0
    var feel : String = ""
    var question : String = ""
   // var questionId : String = ""
    var question_sequence : Int = 0

    interface QuestionInterface{
        fun onAnswerClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignQuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]

        holder.binding.questionTV.text = model.question
        question = holder.binding.questionTV.text.toString()
        question_sequence = position

        Constants.answerList[position] = list[position]

        feel = model.selectedAnswer
        when (model.selectedAnswer)
        {
            "Very Happy" -> {
                mode = 5
                setSelector(holder.binding.option01, holder.binding.option02,holder.binding.option03, holder.binding.option04, holder.binding.option05)
            }
            "Happy" -> {
                mode = 4
                setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
            }
            "Uneasy" -> {
                mode = 3
                setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
            }
            "Sad" -> {
                mode = 2
                setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
            }
            "Angry" -> {
                mode = 1
                setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)
            }
        }


        holder.binding.option01.setOnClickListener{
            setSelector(holder.binding.option01, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option05)
            mode = 5
            feel = "Very Happy"
            Constants.answerList[position] = QuestionModel(model.indexing, model.question, feel, model.currentId)
            listener.onAnswerClicked()
        }

        holder.binding.option02.setOnClickListener{
            setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
            mode = 4
            feel = "Happy"
            Constants.answerList[position] = QuestionModel(model.indexing, model.question, feel, model.currentId)
            listener.onAnswerClicked()
        }

        holder.binding.option03.setOnClickListener{
            setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
            mode = 3
            feel = "Uneasy"
            Constants.answerList[position] = QuestionModel(model.indexing, model.question, feel, model.currentId)
            listener.onAnswerClicked()
        }

        holder.binding.option04.setOnClickListener{
            setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
            mode = 2
            feel = "Sad"
            Constants.answerList[position] = QuestionModel(model.indexing, model.question, feel, model.currentId)
            listener.onAnswerClicked()
        }

        holder.binding.option05.setOnClickListener{
            setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)
            mode = 1
            feel = "Angry"
            Constants.answerList[position] = QuestionModel(model.indexing, model.question, feel, model.currentId)
            listener.onAnswerClicked()
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

}
