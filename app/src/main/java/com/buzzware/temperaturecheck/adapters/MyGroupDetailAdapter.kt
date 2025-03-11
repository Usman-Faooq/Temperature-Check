package com.buzzware.temperaturecheck.adapters

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.ItemDesignGroupMembersBinding
import com.buzzware.temperaturecheck.model.UserModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class MyGroupDetailAdapter(val context : Context,val arrayList : ArrayList<UserModel>,val listener : OnClicked) : RecyclerView.Adapter<MyGroupDetailAdapter.ViewHolder>() {


    private val progressList = ArrayList<Int>()

    interface OnClicked{
        fun OnItemClikced(model: UserModel)
        fun OnMessageIconClicked(id: String, userName: String)
        fun OnDeleteIconClicked(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDesignGroupMembersBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayList[position]

        holder.binding.userName.text = list.userName

        Log.d("LOGGER","${arrayList.size}")

        val layerDrawable = holder.binding.progressSlider.progressDrawable as LayerDrawable
        val clipDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress) as ClipDrawable
        val progressDrawable = clipDrawable.drawable as GradientDrawable


        Glide.with(context)
            .load(list.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.binding.userRoundedImageView)

        when(list.userprogress.toInt())
        {
            0 -> {
                holder.binding.progressSlider.progress = 0

            }
            1 -> {
                holder.binding.progressSlider.progress = 20
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.angry))

            }
            2 -> {
                holder.binding.progressSlider.progress = 40
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.sad))

            }
            3 -> {
                holder.binding.progressSlider.progress = 60
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.uneasy))

            }
            4 -> {
                holder.binding.progressSlider.progress = 80
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.happy))

            }
            5 -> {
                holder.binding.progressSlider.progress = 100
                progressDrawable.setColor(ContextCompat.getColor(context, R.color.very_happy))

            }
        }

        holder.binding.root.setOnClickListener {
            Log.d("LOGER", "Root CLick")
            listener.OnItemClikced(list)
        }
        holder.binding.chatIV.setOnClickListener {
            listener.OnMessageIconClicked(list.id,list.userName)
        }
        holder.binding.deleteIV.setOnClickListener {
            listener.OnDeleteIconClicked(list.id)
            arrayList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }


       // getTodayUserQuestions(list.id,holder.binding.progressSlider)
    }


    fun getTodayUserQuestions(userId: String, progressSlider: SeekBar) {
        var total = 0

        progressList.clear()
        val db = FirebaseFirestore.getInstance()

        val currentTimeStamp = System.currentTimeMillis()

        db.collection("UserQuestions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->


                val model = result.toObjects(UserQuestionModel::class.java) as ArrayList

                model.forEach {
                    if(isSameDay(currentTimeStamp,it.date))
                    {
                        when(it.answer)
                        {
                            "Very Happy" -> progressList.add(5)
                            "Happy" -> progressList.add(4)
                            "Uneasy" -> progressList.add(3)
                            "Sad" -> progressList.add(2)
                            "Angry" -> progressList.add(1)
                        }
                    }
                }

                val avg = if (progressList.isNotEmpty()) progressList.sum()/progressList.size else 0

                if (avg != 0)
                {
                    when(avg)
                    {
                        1 -> {
                            progressSlider.progress = 20
                        }
                        2 -> {
                            progressSlider.progress = 40
                        }
                        3 -> {
                            progressSlider.progress = 60
                        }
                        4 -> {
                            progressSlider.progress = 80
                        }
                        5 -> {
                            progressSlider.progress = 100
                        }
                    }
                    Log.d("LOGGER","${avg}")
                }else
                {
                    progressSlider.progress = 0
                }

            }
            .addOnFailureListener { e ->
                println("Error fetching data: ${e.message}")
            }
    }

    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }





    inner class ViewHolder(val binding : ItemDesignGroupMembersBinding) : RecyclerView.ViewHolder(binding.root)


}