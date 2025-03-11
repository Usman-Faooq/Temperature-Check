package com.buzzware.temperaturecheck.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.StartCheckInActivity
import com.buzzware.temperaturecheck.adapters.CheckInAdapter
import com.buzzware.temperaturecheck.adapters.DaysInRowAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentHomeBinding
import com.buzzware.temperaturecheck.model.ChatModel
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class IndividualHomeFragment : BaseFragment(),DaysInRowAdapter.ItemClicked {

    private lateinit var fragmentContext : Context
    private val binding : FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    var checkInsList : ArrayList<UserQuestionModel> = ArrayList()
    var updateCheckInList : ArrayList<UserQuestionModel> = ArrayList()

    interface ItemClickListeners {
        fun onCheckInClick()
    }

    private lateinit var listener: ItemClickListeners

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setView()
        setListener()
        getQuestionsData()


        updateCheckInList.clear()




        return binding.root
    }

    private fun getQuestionsData() {

        mDialog.show()
        db.collection("UserQuestions")
            .whereEqualTo("userId",Constants.currentUser.id)
            .get()
            .addOnSuccessListener { documents ->
                Constants.currentUserQuestion = documents.toObjects(UserQuestionModel::class.java) as ArrayList
                setAdapter(Constants.currentUserQuestion)
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message.toString())
                mDialog.dismiss()
            }
    }



    private fun setAdapter(currentUserQuestion: java.util.ArrayList<UserQuestionModel>){


        val arraylistDays : ArrayList<String> = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        binding.recyclerView2.layoutManager = LinearLayoutManager(fragmentContext,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView2.adapter = DaysInRowAdapter(fragmentContext, arraylistDays, currentUserQuestion, this)




        val morningScores = mutableListOf<Int>()
        val afternoonScores = mutableListOf<Int>()
        val eveningScores = mutableListOf<Int>()

        Constants.currentUserQuestion.forEach { it ->
            if(isSameDay(System.currentTimeMillis(),it.date.toLong()))
            {
                checkInsList.add(it)

                // Categorize based on time of the day
                when (it.today) {
                    "Morning" -> morningScores.add(getMoodScore(it.answer))
                    "Afternoon" -> afternoonScores.add(getMoodScore(it.answer))
                    "Evening" -> eveningScores.add(getMoodScore(it.answer))
                }
            }
        }


        Log.d("LOGGER","${checkInsList}")

        // Calculate averages
        val morningAvg = if (morningScores.isNotEmpty()) morningScores.sum() / morningScores.size else 0
        val afternoonAvg = if (afternoonScores.isNotEmpty()) afternoonScores.sum() / afternoonScores.size else 0
        val eveningAvg = if (eveningScores.isNotEmpty()) eveningScores.sum() / eveningScores.size else 0

        if (morningAvg > 0){
            val model = checkInsList.firstOrNull { it.today == "Morning" }
            model!!.avg = morningAvg.toDouble()
            updateCheckInList.add(model)
        }

        if (afternoonAvg > 0){
            val model = checkInsList.firstOrNull { it.today == "Afternoon" }
            model!!.avg = afternoonAvg.toDouble()
            updateCheckInList.add(model!!)
        }

        if (eveningAvg > 0){
            val model = checkInsList.firstOrNull { it.today == "Evening" }
            model!!.avg = eveningAvg.toDouble()
            updateCheckInList.add(model!!)
        }

        // Log the averages
        Log.d("AVG_MOOD", "Morning Avg: $morningAvg")
        Log.d("AVG_MOOD", "Afternoon Avg: $afternoonAvg")
        Log.d("AVG_MOOD", "Evening Avg: $eveningAvg")

        setTodaysAvgmode(morningAvg,eveningAvg,afternoonAvg)
        binding.checkInRV.layoutManager = LinearLayoutManager(fragmentContext)
        binding.checkInRV.adapter = CheckInAdapter(fragmentContext, updateCheckInList)
        mDialog.dismiss()

    }

    private fun setTodaysAvgmode(morningAvg: Int, eveningAvg: Int, afternoonAvg: Int) {

        val nonZeroCount = listOf(morningAvg, eveningAvg, afternoonAvg).count { it != 0 }
        val divisor = if (nonZeroCount == 0) 1 else nonZeroCount

        val total_Avg = (morningAvg + eveningAvg + afternoonAvg)/divisor

        when (total_Avg) {
            5 -> {
                binding.personIV5.visibility = View.VISIBLE
                Glide.with(this)
                    .load(Constants.currentUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV5)
            }
            4 -> {
                //color = "happy"
                binding.personIV4.visibility = View.VISIBLE
                Glide.with(this)
                    .load(Constants.currentUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV4)

            }
            3 -> {
                binding.personIV3.visibility = View.VISIBLE
                Glide.with(this)
                    .load(Constants.currentUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV3)

            }
            2 -> {
                binding.personIV2.visibility = View.VISIBLE
                Glide.with(this)
                    .load(Constants.currentUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV2)

            }
            1 -> {
                binding.personIV1.visibility = View.VISIBLE
                Glide.with(this)
                    .load(Constants.currentUser.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.personIV1)
            }
        }

    }


    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }


    private fun setView() {

        Glide.with(this)
            .load(Constants.currentUser.image)
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.userimage)


    }

    private fun setListener() {

        binding.checkInViewAllTV.setOnClickListener {
           listener.onCheckInClick()
        }


        binding.startCheckIn.setOnClickListener{
            startActivity(Intent(fragmentContext, StartCheckInActivity::class.java))
            requireActivity().overridePendingTransition(fadeIn, fadeOut)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
        try {
            listener = context as ItemClickListeners
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }


    override fun onItemClicked(date : CharSequence) {
        if (date != getCurrentDate())
        {
            val intent = Intent(requireActivity(), StartCheckInActivity::class.java)
            intent.putExtra("missDate",date.toString())
            startActivity(intent)
            requireActivity().overridePendingTransition(fadeIn, fadeOut)
        }

    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getMoodScore(answer: String?): Int {
        return when (answer) {
            "Very Happy" -> 5
            "Happy" -> 4
            "Uneasy" -> 3
            "Sad" -> 2
            "Angry" -> 1
            else -> 0
        }
    }


}