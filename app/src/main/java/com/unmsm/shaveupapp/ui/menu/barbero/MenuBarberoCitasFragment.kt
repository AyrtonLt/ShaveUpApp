package com.unmsm.shaveupapp.ui.menu.barbero


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

import com.unmsm.shaveupapp.adapterReservas.ReservaItem
import com.unmsm.shaveupapp.adapterReservas.ReservaItemAdapter
import com.unmsm.shaveupapp.adapterReservasPropuestas.ReservaPropuestaItem
import com.unmsm.shaveupapp.adapterReservasPropuestas.ReservaPropuestaItemAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoCitasBinding
import java.text.SimpleDateFormat
import java.util.Locale


class MenuBarberoCitasFragment : Fragment() {

    private var _binding: FragmentMenuBarberoCitasBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoCitasBinding.inflate(layoutInflater, container, false)

        getReservasPropuestas()
        getReservasAceptadas()
        getReservasCanceladas()
        getReservasTerminadas()

        return binding.root
    }

    private fun reloadData() {
        getReservasPropuestas()
        getReservasAceptadas()
        getReservasCanceladas()
        getReservasTerminadas()
    }

    private fun getReservasPropuestas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaPropuestaItem>()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "1" && document.getString("barberroId") == userId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val reserva = ReservaPropuestaItem(
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

                // Ordenar la lista de reservas por fecha desde la más reciente a la más antigua
                reservas.sortByDescending {
                    dateFormat.parse(it.fecha)
                }

                binding.rvReservasPropuestas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasPropuestas.adapter = ReservaPropuestaItemAdapter(
                    reservas,
                    { reservaPropuestaItem -> onAcceptReservaItemSelected(reservaPropuestaItem) },
                    { reservaPropuestaItem -> onCancelReservaItemSelected(reservaPropuestaItem) })
            }
        }
    }

    private fun onCancelReservaItemSelected(reservaPropuestaItem: ReservaPropuestaItem) {
        db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("citas").document(reservaPropuestaItem.reservaId)

        // Actualizar la propiedad "estado"
        documentRef.update("estado", "3")
            .addOnSuccessListener {
                // Exito
                Toast.makeText(
                    requireContext(),
                    "Estado de la reserva actualizado",
                    Toast.LENGTH_LONG
                ).show()
                reloadData()
            }
            .addOnFailureListener { e ->
                // Fallo
                println("Error al actualizar el estado: $e")
            }
    }

    private fun onAcceptReservaItemSelected(reservaPropuestaItem: ReservaPropuestaItem) {

        db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("citas").document(reservaPropuestaItem.reservaId)

        // Actualizar la propiedad "estado"
        documentRef.update("estado", "2")
            .addOnSuccessListener {
                // Exito
                Toast.makeText(
                    requireContext(),
                    "Estado de la reserva actualizado",
                    Toast.LENGTH_LONG
                ).show()
                reloadData()
            }
            .addOnFailureListener { e ->
                // Fallo
                println("Error al actualizar el estado: $e")
            }
    }

    //Pasa a estado Terminado
    private fun onFinishItemSelected(reservaItem: ReservaItem) {
        db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("citas").document(reservaItem.reservaId)

        // Actualizar la propiedad "estado"
        documentRef.update("estado", "4")
            .addOnSuccessListener {
                // Exito
                Toast.makeText(
                    requireContext(),
                    "Estado de la reserva actualizado",
                    Toast.LENGTH_LONG
                ).show()
                reloadData()
            }
            .addOnFailureListener { e ->
                // Fallo
                println("Error al actualizar el estado: $e")
            }
    }

    private fun getReservasAceptadas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "2" && document.getString("barberroId") == userId) {
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

                // Ordenar la lista de reservas por fecha desde la más reciente a la más antigua
                reservas.sortByDescending {
                    dateFormat.parse(it.fecha)
                }

                binding.rvReservasAceptadas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasAceptadas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaItem -> onFinishItemSelected(reservaItem) })
            }
        }
    }

    private fun getReservasCanceladas() {
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("estado") == "3" && document.getString("barberroId") == userId) {
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

                // Ordenar la lista de reservas por fecha desde la más reciente a la más antigua
                reservas.sortByDescending {
                    dateFormat.parse(it.fecha)
                }

                binding.rvReservasCanceladas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasCanceladas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaItem -> onReservaItemSelected(reservaItem) })
            }
        }
    }

    // Obtiene reservas terminadas
    private fun getReservasTerminadas(){
        db = FirebaseFirestore.getInstance()
        db.collection("citas").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val reservas = mutableListOf<ReservaItem>()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if ((document.getString("estado") == "4" || document.getString("estado") == "5") && document.getString("barberroId") == userId) {
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

                // Ordenar la lista de reservas por fecha desde la más reciente a la más antigua
                reservas.sortByDescending {
                    dateFormat.parse(it.fecha)
                }

                binding.rvReservasTerminadas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvReservasTerminadas.adapter = ReservaItemAdapter(
                    reservas,
                    { reservaItem -> onReservaItemSelected(reservaItem) })
            }
        }
    }

    private fun onReservaItemSelected(reservaItem: ReservaItem) {
        Toast.makeText(requireContext(), reservaItem.reservaId, Toast.LENGTH_SHORT).show()
    }
}