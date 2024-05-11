package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.data.Barbero
import com.unmsm.shaveupapp.data.BarberoListAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteBusquedaBinding

class MenuClienteBusquedaFragment : Fragment() {

    private var _binding: FragmentMenuClienteBusquedaBinding? = null
    private val binding get() = _binding!!
    private lateinit var barberoList: ArrayList<Barbero>
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteBusquedaBinding.inflate(layoutInflater, container, false)

        //getBarbero()

        //binding.rvBarberoList.adapter = BarberoListAdapter(barberoList)

        return binding.root
    }

    private fun getBarbero() {
        db = FirebaseFirestore.getInstance()
        db.collection("usuario").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    val barbero: Barbero? = data.toObject(Barbero::class.java)
                    if (barbero != null) {
                        barberoList.add(barbero)
                    }
                }
            }
        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}