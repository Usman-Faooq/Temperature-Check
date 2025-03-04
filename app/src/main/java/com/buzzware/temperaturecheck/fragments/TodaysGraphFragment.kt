package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.classes.Constants
import com.buzzware.temperaturecheck.databinding.FragmentTodaysGraphBinding


class TodaysGraphFragment(val res : Int) : Fragment() {

    private val binding : FragmentTodaysGraphBinding by lazy {
        FragmentTodaysGraphBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        
        setViews()



        return binding.root
    }

    private fun setViews() {
        if(res == 1) {
            binding.personIV5.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV5)
        }else if(res == 2) {
            binding.personIV4.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV4)

        }else if(res == 3) {
            binding.personIV3.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV3)

        }else if(res == 4) {
            binding.personIV2.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV2)

        }else if(res == 5) {
            binding.personIV1.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constants.currentUser.image)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.personIV1)

        }
    }
}