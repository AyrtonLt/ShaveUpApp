package com.unmsm.shaveupapp.ui.menu.barbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoCitasBinding

class MenuBarberoCitasFragment : Fragment() {

    private var _binding: FragmentMenuBarberoCitasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoCitasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}