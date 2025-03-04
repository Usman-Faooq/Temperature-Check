package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.adapters.ChatingViewPagerAdapter
import com.buzzware.temperaturecheck.databinding.FragmentChatBinding
import com.buzzware.temperaturecheck.model.ChatModel

class ChatFragment : BaseFragment() {

    val binding : FragmentChatBinding by lazy {
        FragmentChatBinding.inflate(layoutInflater)
    }



    private lateinit var chatingViewPagerAdapter : ChatingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{


        setView()
        setListener()


        return binding.root
    }





    private fun setView() {
        binding.chatTV.setBackgroundResource(R.drawable.check_in_bg_today)
        val fragment = listOf(PersonalChatFragment())
        chatingViewPagerAdapter = ChatingViewPagerAdapter(fragment,this)
        binding.viewPager.adapter = chatingViewPagerAdapter
    }

    private fun setListener() {
        binding.chatTV.setOnClickListener {
            binding.chatTV.setBackgroundResource(R.drawable.check_in_bg_today)
            binding.groupTV.setBackgroundResource(0)
            chatingViewPagerAdapter.updateFragments(listOf(PersonalChatFragment()))
        }

        binding.groupTV.setOnClickListener {
            binding.groupTV.setBackgroundResource(R.drawable.check_in_bg_month)
            binding.chatTV.setBackgroundResource(0)
            chatingViewPagerAdapter.updateFragments(listOf(GroupChatFragment()))

        }
    }
}