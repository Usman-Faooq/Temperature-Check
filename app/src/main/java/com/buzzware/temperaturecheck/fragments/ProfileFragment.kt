package com.buzzware.temperaturecheck.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.activities.EditProfileActivity
import com.buzzware.temperaturecheck.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment() {

    private val binding : FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        setView()
        setListener()

        return binding.root
    }

    private fun setView() {

    }

    private fun setListener() {

        binding.editProfileTV.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
            requireActivity().overridePendingTransition(fadeIn, fadeOut)
        }
    }
}