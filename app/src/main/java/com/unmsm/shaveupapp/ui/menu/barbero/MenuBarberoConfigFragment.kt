package com.unmsm.shaveupapp.ui.menu.barbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoConfigBinding

class MenuBarberoConfigFragment : Fragment() {

    private var _binding: FragmentMenuBarberoConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoConfigBinding.inflate(layoutInflater, container, false)

        binding.btnEditInfo.setOnClickListener {
            findNavController().navigate(R.id.action_menuBarberoConfigFragment_to_configInfoFragment2)
        }

        binding.btnLogout.setOnClickListener {
            activity?.finishAffinity()
        }

        return binding.root
    }

}