package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterReservas.ReservaItem
import com.unmsm.shaveupapp.adapterReservas.ReservaItemAdapter
import com.unmsm.shaveupapp.adapterReservasPropuestas.ReservaPropuestaItem
import com.unmsm.shaveupapp.adapterReservasPropuestas.ReservaPropuestaItemAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteCitasBinding

class MenuClienteCitasFragment : Fragment() {

    private var _binding: FragmentMenuClienteCitasBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteCitasBinding.inflate(layoutInflater, container, false)

        getReservasPropuestas()
        getReservasAceptadas()
        getReservasCanceladas()

        return binding.root
    }

    private fun getReservasPropuestas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "1" && document.getString("userId") == userId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val reserva = ReservaItem(
                            reservaId = document.getString("reservaId") ?: "",
                            estado = document.getString("estado") ?: "",
                            userId = document.getString("userId") ?: "",
                            barberroId = document.getString("barberroId") ?: "",
                            hora = document.getString("hora") ?: "",
                            fecha = document.getString("fecha") ?: "",
                            serviciosList = document.get("serviciosList") as? List<String>
                                ?: listOf()
                        )
                        // Agregar el objeto Barbero a la lista
                        reservas.add(reserva)
                    }
                }
                binding.rvReservasPropuestas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasPropuestas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaPropuestaItem -> onReservaItemSelected(reservaPropuestaItem) })
            }
        }
    }

    private fun getReservasAceptadas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "2" && document.getString("userId") == userId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val reserva = ReservaItem(
                            reservaId = document.getString("reservaId") ?: "",
                            estado = document.getString("estado") ?: "",
                            userId = document.getString("userId") ?: "",
                            barberroId = document.getString("barberroId") ?: "",
                            hora = document.getString("hora") ?: "",
                            fecha = document.getString("fecha") ?: "",
                            serviciosList = document.get("serviciosList") as? List<String>
                                ?: listOf()
                        )
                        // Agregar el objeto Barbero a la lista
                        reservas.add(reserva)
                    }
                }
                binding.rvReservasAceptadas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasAceptadas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaItem -> onReservaItemSelected(reservaItem) })
            }
        }
    }

    private fun getReservasCanceladas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "3" && document.getString("userId") == userId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val reserva = ReservaItem(
                            reservaId = document.getString("reservaId") ?: "",
                            estado = document.getString("estado") ?: "",
                            userId = document.getString("userId") ?: "",
                            barberroId = document.getString("barberroId") ?: "",
                            hora = document.getString("hora") ?: "",
                            fecha = document.getString("fecha") ?: "",
                            serviciosList = document.get("serviciosList") as? List<String>
                                ?: listOf()
                        )
                        // Agregar el objeto Barbero a la lista
                        reservas.add(reserva)
                    }
                }
                binding.rvReservasCanceladas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasCanceladas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaItem -> onReservaItemSelected(reservaItem) })
            }
        }
    }

    private fun onReservaItemSelected(reservaItem: ReservaItem) {
        Toast.makeText(requireContext(), reservaItem.reservaId, Toast.LENGTH_SHORT).show()
    }


}