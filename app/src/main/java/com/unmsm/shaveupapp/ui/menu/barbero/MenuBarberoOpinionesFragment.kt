package com.unmsm.shaveupapp.ui.menu.barbero

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoOpinionesBinding
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.adapterComentario.ComentarioItemAdapter
import com.unmsm.shaveupapp.adapterReservas.ReservaItemAdapter
import com.unmsm.shaveupapp.ui.login.LoginActivity

class MenuBarberoOpinionesFragment : Fragment() {

    private var _binding: FragmentMenuBarberoOpinionesBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    //ID DEL BARBERO
    private val barberoId = LoginActivity.DatosCompartidos.obtenerBarberoId()

    // lista de prueba
    private val listaComentarios = listOf(
        ComentarioItem(
            comentarioId = "1",
            userId = "user001",
            userName = "Juan Pérez",
            barberoId = "barber001",
            comentario = "Excelente servicio, me encantó el corte de cabello.",
            servicios = "Corte de cabello",
            puntuacion = "5"
        ),
        ComentarioItem(
            comentarioId = "2",
            userId = "user002",
            userName = "María García",
            barberoId = "barber002",
            comentario = "El barbero fue muy amable y profesional. Recomendado.",
            servicios = "Corte de cabello, arreglo de barba",
            puntuacion = "4"
        ),
        ComentarioItem(
            comentarioId = "3",
            userId = "user003",
            userName = "Carlos Rodríguez",
            barberoId = "barber001",
            comentario = "Quedé muy satisfecho con el resultado del corte de cabello.",
            servicios = "Corte de cabello",
            puntuacion = "5"
        ),
        ComentarioItem(
            comentarioId = "4",
            userId = "user004",
            userName = "Ana López",
            barberoId = "barber003",
            comentario = "El servicio fue regular, esperaba un mejor resultado.",
            servicios = "Corte de cabello, tinte",
            puntuacion = "3"
        ),
        ComentarioItem(
            comentarioId = "5",
            userId = "user005",
            userName = "Pedro Martínez",
            barberoId = "barber002",
            comentario = "Excelente atención al cliente y gran habilidad del barbero.",
            servicios = "Corte de cabello, afeitado",
            puntuacion = "5"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoOpinionesBinding.inflate(layoutInflater, container, false)

        getComentarios()

        return binding.root
    }

    private fun getComentarios() {
        // Aca jalas los comentarios de la base de datos

        binding.rvComentarios.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComentarios.adapter = ComentarioItemAdapter(
            listaComentarios,
            { comentarioItem -> onItemSelected(comentarioItem) })
    }

    private fun onItemSelected(comentarioItem: ComentarioItem){

    }

}