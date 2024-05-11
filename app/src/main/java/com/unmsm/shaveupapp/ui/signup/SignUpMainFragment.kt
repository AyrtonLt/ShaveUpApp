package com.unmsm.shaveupapp.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentSignUpMainBinding

class SignUpMainFragment : Fragment() {

    private var _binding: FragmentSignUpMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpMainBinding.inflate(layoutInflater, container, false)

        binding.btnChooseCliente.setOnClickListener {
            findNavController().navigate(R.id.action_signUpMainFragment_to_signUpClienteFragment)
        }

        binding.btnChooseBarbero.setOnClickListener {
            findNavController().navigate(R.id.action_signUpMainFragment_to_signUpBarberoFragment)
        }

        return binding.root
    }

}