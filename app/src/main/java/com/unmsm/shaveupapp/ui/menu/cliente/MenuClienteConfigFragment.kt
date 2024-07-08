package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterLanguage.LanguageManager
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteConfigBinding

class MenuClienteConfigFragment : Fragment() {

    private var _binding: FragmentMenuClienteConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteConfigBinding.inflate(layoutInflater, container, false)
        LanguageManager.updateLocale(requireContext(), LanguageManager.getSelectedLanguage(requireContext()))

        binding.btnEditInfo.setOnClickListener {
            findNavController().navigate(R.id.action_menuClienteConfigFragment_to_configInfoFragment)
        }

        binding.btnLogout.setOnClickListener {
            activity?.finishAffinity()
        }

        binding.switchLanguage.isChecked = LanguageManager.getSelectedLanguage(requireContext()) == "es"
        binding.switchLanguage.setOnCheckedChangeListener { buttonView, isChecked ->
            val newLanguage = if (isChecked) "es" else "en"
            if (LanguageManager.getSelectedLanguage(requireContext()) != newLanguage) {
                LanguageManager.setSelectedLanguage(requireContext(), newLanguage)
                LanguageManager.updateLocale(requireContext(), newLanguage)
                requireActivity().recreate() // Recrear la actividad para aplicar el nuevo idioma
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}