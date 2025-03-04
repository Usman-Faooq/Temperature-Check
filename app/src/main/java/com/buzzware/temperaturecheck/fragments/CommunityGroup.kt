package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.FragmentCommunityGroupBinding
import com.buzzware.temperaturecheck.databinding.FragmentMyGroupCommunityBinding


class CommunityGroup : Fragment() {

    private val binding : FragmentCommunityGroupBinding by lazy {
        FragmentCommunityGroupBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{





        return binding.root
    }
}