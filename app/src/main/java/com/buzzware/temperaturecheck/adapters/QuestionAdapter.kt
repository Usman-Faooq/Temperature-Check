package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ItemDesignQuestionLayoutBinding

class QuestionAdapter(val context: Context, private val list: ArrayList<String>) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignQuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.questionTV.text = list[position]

        if (position != 0){
            holder.binding.option05.setImageResource(R.drawable.emoji_5)
        }

        holder.binding.option01.setOnClickListener {
            setSelector(holder.binding.option01, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option05)
        }

        holder.binding.option02.setOnClickListener {
            setSelector(holder.binding.option02, holder.binding.option01, holder.binding.option03, holder.binding.option04, holder.binding.option05)
        }

        holder.binding.option03.setOnClickListener {
            setSelector(holder.binding.option03, holder.binding.option02, holder.binding.option01, holder.binding.option04, holder.binding.option05)
        }

        holder.binding.option04.setOnClickListener {
            setSelector(holder.binding.option04, holder.binding.option02, holder.binding.option03, holder.binding.option01, holder.binding.option05)
        }

        holder.binding.option05.setOnClickListener {
            setSelector(holder.binding.option05, holder.binding.option02, holder.binding.option03, holder.binding.option04, holder.binding.option01)
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
