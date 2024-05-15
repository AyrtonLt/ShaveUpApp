package com.unmsm.shaveupapp.ui.menu.barbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapter.BarberoItem
import com.unmsm.shaveupapp.adapter.BarberoItemAdapter
import com.unmsm.shaveupapp.adapterServicios.ServicioItem
import com.unmsm.shaveupapp.adapterServicios.ServicioItemAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoProfileBinding

class MenuBarberoProfileFragment : Fragment() {

    private var _binding: FragmentMenuBarberoProfileBinding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoProfileBinding.inflate(layoutInflater, container, false)

        getUserData()
        getServiciosData()

        binding.btnCreateServicio.setOnClickListener {
            findNavController().navigate(R.id.action_menuBarberoProfileFragment_to_createServicioFragment)
        }

        return binding.root
    }

    private fun getServiciosData() {
        db = FirebaseFirestore.getInstance()
        db.collection("servicio").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Barbero
            val servicios = mutableListOf<ServicioItem>()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("userBarbero") == userId.toString()) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val servicio = ServicioItem(
                            nombreServicio = document.getString("name") ?: "",
                            descripcionServicio = document.getString("desc") ?: "",
                            precioServicio = document.getString("price") ?: "",
                            isSelected = false
                        )
                        // Agregar el objeto Barbero a la lista
                        servicios.add(servicio)
                    }
                }
                binding.rvServicio.layoutManager = LinearLayoutManager(requireContext())
                binding.rvServicio.adapter = ServicioItemAdapter(servicios)
            }
        }
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("usuario").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val firstName = it.data?.get("nombre")?.toString()
                val lastName = it.data?.get("apellido")?.toString()
                val barberiaName = it.data?.get("barberiaNombre")?.toString()
                val phone = it.data?.get("telefono")?.toString()
                val location = it.data?.get("direccion")?.toString()

                binding.tvName.setText(firstName + " " + lastName)
                binding.tvBarberiaName.setText(barberiaName)
                binding.tvPhone.setText(phone)
                binding.tvLocation.setText(location)

            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
    }

}