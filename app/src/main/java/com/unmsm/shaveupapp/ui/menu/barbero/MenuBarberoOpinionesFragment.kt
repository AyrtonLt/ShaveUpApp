package com.unmsm.shaveupapp.ui.menu.barbero

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoOpinionesBinding
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.adapterComentario.ComentarioItemAdapter
import com.unmsm.shaveupapp.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale

class MenuBarberoOpinionesFragment : Fragment() {

    private var _binding: FragmentMenuBarberoOpinionesBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    //ID DEL BARBERO
    private val barberoId = LoginActivity.DatosCompartidos.obtenerBarberoId()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoOpinionesBinding.inflate(layoutInflater, container, false)

        getComentarios()

        return binding.root
    }

    private fun getComentarios() {
        db = FirebaseFirestore.getInstance()
        db.collection("comentarios").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val comentarios = mutableListOf<ComentarioItem>()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("barberoId") == barberoId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val comentario = ComentarioItem(
                            comentarioId = document.getString("comentarioId") ?: "",
                            userId = document.getString("userId") ?: "",
                            userName = document.getString("userName") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            comentario = document.getString("comentario") ?: "",
                            servicios = document.getString("servicios") ?: "",
                            puntuacion = document.getString("puntuacion") ?: "",
                            photoUrl = document.getString("photoUrl") ?: "",
                            fecha = document.getString("fecha") ?: ""
                        )
                        // Agregar el objeto Barbero a la lista
                        comentarios.add(comentario)
                    }
                }
                comentarios.sortByDescending {
                    dateFormat.parse(it.fecha)
                }
                binding.rvComentarios.layoutManager = LinearLayoutManager(requireContext())
                binding.rvComentarios.adapter = ComentarioItemAdapter(
                    comentarios,
                    { comentarioItem -> onItemSelected(comentarioItem) })
            }
        }
    }

    private fun onItemSelected(comentarioItem: ComentarioItem) {
        Log.i("000000000000000000000000000000000000000000000000", comentarioItem.photoUrl)


        if (comentarioItem.photoUrl.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Este comentario no posee foto",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val intent = Intent(requireContext(), FullPhotoActivity::class.java).apply {
                putExtra(
                    "urlPhoto",
                    comentarioItem.photoUrl
                ) // Reemplaza "clave" por la clave que desees y "valor" por el valor que quieras enviar
            }
            startActivity(intent)
        }


    }

}