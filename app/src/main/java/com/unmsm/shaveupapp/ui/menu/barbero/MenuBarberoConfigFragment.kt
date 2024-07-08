package com.unmsm.shaveupapp.ui.menu.barbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.fragment.findNavController
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterLanguage.LanguageManager
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoConfigBinding

class MenuBarberoConfigFragment : Fragment() {

    private var _binding: FragmentMenuBarberoConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoConfigBinding.inflate(layoutInflater, container, false)
        LanguageManager.updateLocale(requireContext(), LanguageManager.getSelectedLanguage(requireContext()))
        binding.btnEditInfo.setOnClickListener {
            findNavController().navigate(R.id.action_menuBarberoConfigFragment_to_configInfoFragment2)
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