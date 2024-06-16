package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentFindNearbyBarberBinding

class FindNearbyBarberFragment : Fragment() {

    private var _binding: FragmentFindNearbyBarberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindNearbyBarberBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}