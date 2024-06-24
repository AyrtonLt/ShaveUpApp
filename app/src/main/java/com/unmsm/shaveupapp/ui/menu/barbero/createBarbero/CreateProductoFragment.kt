package com.unmsm.shaveupapp.ui.menu.barbero.createBarbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentCreateProductoBinding

class CreateProductoFragment : Fragment() {

    private var _binding: FragmentCreateProductoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProductoBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

}