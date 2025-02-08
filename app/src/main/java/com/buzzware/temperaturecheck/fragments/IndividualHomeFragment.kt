package com.buzzware.temperaturecheck.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.StartCheckInActivity
import com.buzzware.temperaturecheck.activities.TeamPerformanceActivity
import com.buzzware.temperaturecheck.adapters.AddTeamAdapter
import com.buzzware.temperaturecheck.adapters.CheckInAdapter
import com.buzzware.temperaturecheck.adapters.TeamAdapter
import com.buzzware.temperaturecheck.databinding.FragmentCommunityBinding
import com.buzzware.temperaturecheck.databinding.FragmentHomeBinding

class IndividualHomeFragment : BaseFragment() {

    private val binding : FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    interface ItemClickListeners {
        fun onCheckInClick()
    }

    private lateinit var listener: ItemClickListeners

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setView()
        setListener()

        return binding.root
    }

    private fun setView() {

        binding.constraintLayout3.visibility = View.GONE
        binding.recyclerView2.visibility = View.GONE

        binding.checkInRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.checkInRV.adapter = CheckInAdapter(requireActivity(), arrayListOf("1", "2"))

    }

    private fun setListener() {

        binding.checkInViewAllTV.setOnClickListener {
            listener.onCheckInClick()
        }

        binding.startCheckIn.setOnClickListener {
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
}