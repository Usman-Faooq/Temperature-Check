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
import com.buzzware.temperaturecheck.adapters.ChatingViewPagerAdapter
import com.buzzware.temperaturecheck.adapters.CommunityAdapter
import com.buzzware.temperaturecheck.databinding.FragmentCommunityBinding
import com.buzzware.temperaturecheck.databinding.FragmentProfileBinding

class CommunityFragment : BaseFragment() {

    private val binding : FragmentCommunityBinding by lazy {
        FragmentCommunityBinding.inflate(layoutInflater)
    }
    private lateinit var communityAdapter : CommunityAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setView()
        setListener()

        return binding.root
    }

    private fun setView() {
        binding.myGroupTV.setBackgroundResource(R.drawable.check_in_bg_today)
        val fragment = listOf(MyGroupCommunity())
        communityAdapter = CommunityAdapter(requireActivity(),fragment)
        binding.viewPagerCommunity.adapter = communityAdapter
    }

    private fun setListener() {
        binding.myGroupTV.setOnClickListener {
            binding.myGroupTV.setBackgroundResource(R.drawable.check_in_bg_today)
            binding.CommunityGroupTV.setBackgroundResource(0)
            communityAdapter.updateFragments(listOf(MyGroupCommunity()))
        }

        binding.CommunityGroupTV.setOnClickListener {
            binding.CommunityGroupTV.setBackgroundResource(R.drawable.check_in_bg_month)
            binding.myGroupTV.setBackgroundResource(0)
            communityAdapter.updateFragments(listOf(CommunityGroup()))

        }

    }

}