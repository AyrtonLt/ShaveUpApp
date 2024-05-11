package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteConfigBinding

class MenuClienteConfigFragment : Fragment() {

    private var _binding: FragmentMenuClienteConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteConfigBinding.inflate(layoutInflater, container, false)

        binding.btnEditInfo.setOnClickListener {
            findNavController().navigate(R.id.action_menuClienteConfigFragment_to_configInfoFragment)
        }


        return binding.root
    }
}