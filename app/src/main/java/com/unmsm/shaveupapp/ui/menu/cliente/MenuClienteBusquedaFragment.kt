package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.data.Barbero
import com.unmsm.shaveupapp.data.BarberoListAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteBusquedaBinding

class MenuClienteBusquedaFragment : Fragment() {

    private var _binding: FragmentMenuClienteBusquedaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteBusquedaBinding.inflate(layoutInflater, container, false)

        binding.rvBarberoList.adapter = BarberoListAdapter(
            listOf(
                Barbero("1", "Ayrton", "Lopez", "Lions", "Calle Wiracocha 135")
            )
        )

        return binding.root
    }
}