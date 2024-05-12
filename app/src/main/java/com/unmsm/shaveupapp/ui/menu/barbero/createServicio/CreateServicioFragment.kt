package com.unmsm.shaveupapp.ui.menu.barbero.createServicio

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentCreateServicioBinding
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoProfileBinding
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class CreateServicioFragment : Fragment() {

    private var _binding: FragmentCreateServicioBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateServicioBinding.inflate(layoutInflater, container, false)

        binding.btnCreateServicio.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val nombreServicio = binding.tietServicioName.text.toString()
            val descServicio = binding.tietServicioDesc.text.toString()
            val priceServicio = binding.tietServicioPrice.text.toString()

            val servicio = mutableMapOf<String, Any>()
            servicio["userBarbero"] = userId
            servicio["name"] = nombreServicio
            servicio["desc"] = descServicio
            servicio["price"] = priceServicio

            FirebaseFirestore.getInstance().collection("servicio").document()
                .set(servicio).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Servicio creado",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Ocurrió un error :(",
                        Toast.LENGTH_SHORT
                    ).show()

                }

        }

        return binding.root
    }
}