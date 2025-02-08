package com.buzzware.temperaturecheck.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.TeamPerformanceActivity
import com.buzzware.temperaturecheck.adapters.CommunityAdapter
import com.buzzware.temperaturecheck.databinding.FragmentCommunityBinding
import com.buzzware.temperaturecheck.databinding.FragmentProfileBinding

class CommunityFragment : BaseFragment() {

    private val binding : FragmentCommunityBinding by lazy {
        FragmentCommunityBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setView()
        setListener()

        return binding.root
    }

    private fun setView() {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = CommunityAdapter(requireActivity(), arrayListOf())

    }

    private fun setListener() {

        binding.teamPreformanceTV.setOnClickListener {
            startActivity(Intent(requireActivity(), TeamPerformanceActivity::class.java))
            requireActivity().overridePendingTransition(fadeIn, fadeOut)
        }

    }

}