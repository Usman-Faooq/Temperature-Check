package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.CheckInAdapter
import com.buzzware.temperaturecheck.databinding.FragmentCheckInBinding
import com.buzzware.temperaturecheck.databinding.FragmentCommunityBinding

class CheckInFragment : Fragment() {

    private val binding : FragmentCheckInBinding by lazy {
        FragmentCheckInBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = CheckInAdapter(requireActivity(), arrayListOf("1", "2", "3"))

        return binding.root
    }
}