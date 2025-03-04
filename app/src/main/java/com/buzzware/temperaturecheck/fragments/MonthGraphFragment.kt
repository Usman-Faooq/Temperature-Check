package com.buzzware.temperaturecheck.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buzzware.temperaturecheck.R
import com.buzzware.temperaturecheck.databinding.FragmentMonthGraphBinding


class MonthGraphFragment : Fragment() {

    private val binding : FragmentMonthGraphBinding by lazy {
        FragmentMonthGraphBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{







        return binding.root
    }
}