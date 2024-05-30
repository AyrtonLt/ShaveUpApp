package com.unmsm.shaveupapp.ui.menu.cliente

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.adapter.BarberoItem
import com.unmsm.shaveupapp.adapter.BarberoItemAdapter
import com.unmsm.shaveupapp.adapterServicios.ServicioItem
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteBusquedaBinding
import com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile.BarberoProfileActivity
import java.util.Locale

class MenuClienteBusquedaFragment : Fragment() {

    private var _binding: FragmentMenuClienteBusquedaBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteBusquedaBinding.inflate(layoutInflater, container, false)

        getBarbero()

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()) {
                    getBarberoWithString(searchText)
                } else {
                    getBarbero()
                }


                return true
            }

        })


        return binding.root
    }

    private fun getBarbero() {

        db = FirebaseFirestore.getInstance()
        db.collection("usuario").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Barbero
            val barberos = mutableListOf<BarberoItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    // Chequear si userType es "2"
                    if (document.getString("userType") == "1") {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val barbero = BarberoItem(
                            userId = document.getString("user_id") ?: "",
                            urlPhoto = document.getString("urlProfilePhoto") ?: "",
                            barberiaName = document.getString("barberiaNombre") ?: "",
                            barberoFullName = document.getString("apellido") ?: "",
                            location = document.getString("direccion") ?: "",
                        )
                        // Agregar el objeto Barbero a la lista
                        barberos.add(barbero)
                    }
                }
                binding.rvBarbero.layoutManager = LinearLayoutManager(requireContext())
                binding.rvBarbero.adapter = BarberoItemAdapter(
                    barberos,
                    { barberoItem -> onBarberoItemSelected(barberoItem) })
            }
        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun onBarberoItemSelected(barberoItem: BarberoItem) {
        Toast.makeText(requireContext(), barberoItem.barberoFullName, Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), BarberoProfileActivity::class.java)
        intent.putExtra("barberoId", barberoItem.barberoFullName)
        intent.putExtra("userId", barberoItem.userId)
        startActivity(intent)
    }

    private fun getBarberoWithString(string: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("usuario").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Barbero
            val barberos = mutableListOf<BarberoItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    // Chequear si userType es "2"
                    if (document.getString("userType") == "1") {
                        // Obtener el nombre completo del barbero
                        val barberoFullName = document.getString("apellido") ?: ""

                        // Verificar si el nombre completo contiene el string buscado
                        if (barberoFullName.contains(string, ignoreCase = true)) {
                            // Crear un nuevo objeto Barbero con los datos del documento
                            val barbero = BarberoItem(
                                userId = document.getString("user_id") ?: "",
                                urlPhoto = document.getString("urlProfilePhoto") ?: "",
                                barberiaName = document.getString("barberiaNombre") ?: "",
                                barberoFullName = document.getString("apellido") ?: "",
                                location = document.getString("direccion") ?: "",
                            )
                            // Agregar el objeto Barbero a la lista
                            barberos.add(barbero)
                        }

                    }
                }
                binding.rvBarbero.layoutManager = LinearLayoutManager(requireContext())
                binding.rvBarbero.adapter = BarberoItemAdapter(
                    barberos,
                    { barberoItem -> onBarberoItemSelected(barberoItem) })
            }
        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}