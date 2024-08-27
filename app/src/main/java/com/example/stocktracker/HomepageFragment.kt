package com.example.stocktracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stocktracker.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment() {


    private lateinit var _binding:FragmentHomepageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentHomepageBinding.inflate(inflater,container,false)



        return _binding.root
    }
}