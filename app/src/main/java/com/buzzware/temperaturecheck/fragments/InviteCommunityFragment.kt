package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.FragmentInviteCommunityBinding


class InviteCommunityFragment : Fragment() {

    private val binding : FragmentInviteCommunityBinding by lazy {
        FragmentInviteCommunityBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{






        return binding.root
    }
}