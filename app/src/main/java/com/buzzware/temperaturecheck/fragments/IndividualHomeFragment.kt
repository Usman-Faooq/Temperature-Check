package com.buzzware.temperaturecheck.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.StartCheckInActivity
import com.buzzware.temperaturecheck.adapters.CheckInAdapter
import com.buzzware.temperaturecheck.adapters.DaysInRowAdapter
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentHomeBinding
import com.buzzware.temperaturecheck.model.UserQuestionModel
import java.util.Calendar

class IndividualHomeFragment : BaseFragment(),DaysInRowAdapter.ItemClicked {

    private val binding : FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    var checkInsList : ArrayList<UserQuestionModel> = ArrayList()

    interface ItemClickListeners {
        fun onCheckInClick()
    }

    private lateinit var listener: ItemClickListeners

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setView()
        setListener()
        getQuestionsData()

        return binding.root
    }

    private fun getQuestionsData() {

        db.collection("UserQuestions")
            .whereEqualTo("userId",Constants.currentUser.id)
            .get()
            .addOnSuccessListener { documents ->
                Constants.currentUserQuestion = documents.toObjects(UserQuestionModel::class.java) as ArrayList
                setAdapter(Constants.currentUserQuestion)
            }
            .addOnFailureListener { exception ->
                showAlert(exception.message.toString())
            }
    }

    private fun setAdapter(currentUserQuestion: java.util.ArrayList<UserQuestionModel>){


        val arraylistDays : ArrayList<String> = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView2.adapter = DaysInRowAdapter(requireActivity(), arraylistDays, currentUserQuestion, this)



        checkInsList.clear()
        Constants.currentUserQuestion.forEach { it ->
            if(isSameDay(System.currentTimeMillis(),it.date.toLong()))
            { checkInsList.add(it) }
        }
        binding.checkInRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.checkInRV.adapter = CheckInAdapter(requireActivity(), checkInsList)


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
            .into(binding.personIV)

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
            startActivity(Intent(requireActivity(), StartCheckInActivity::class.java))
            requireActivity().overridePendingTransition(fadeIn, fadeOut)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ItemClickListeners
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDataPass")
        }
    }


    override fun onItemClicked(dayIcon: ImageView) {
        startActivity(Intent(requireActivity(), StartCheckInActivity::class.java))
        requireActivity().overridePendingTransition(fadeIn, fadeOut)

    }
}