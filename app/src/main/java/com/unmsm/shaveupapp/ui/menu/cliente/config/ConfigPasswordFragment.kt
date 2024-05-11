package com.unmsm.shaveupapp.ui.menu.cliente.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentConfigPasswordBinding

class ConfigPasswordFragment : Fragment() {

    private var _binding: FragmentConfigPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}