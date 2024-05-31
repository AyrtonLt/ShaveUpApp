package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.adapterComentario.ComentarioItemAdapter
import com.unmsm.shaveupapp.adapterPhotoVisit.PhotoItemVisit
import com.unmsm.shaveupapp.adapterPhotoVisit.PhotoItemVisitAdapter
import com.unmsm.shaveupapp.databinding.ActivityBarberoProfileBinding
import com.unmsm.shaveupapp.ui.login.LoginActivity

class BarberoProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarberoProfileBinding

    private var db = Firebase.firestore
    private lateinit var barberId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBarberoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle: Bundle? = intent.extras
        val nmaeBarbero = bundle!!.getString("barberoId")
        barberId = bundle.getString("userId").toString()
        LoginActivity.DatosCompartidos.guardarDatoBarberoId(barberId)
        binding.tvNombreBarvero.text = nmaeBarbero
        getBarbero()
        getServiciosData()
        getPhotos()
        getComentarios()

        binding.btnReservar.setOnClickListener {
            val intent = Intent(this, MakeReservationActivity::class.java)
            intent.putExtra("userId", barberId)
            startActivity(intent)
        }
        //boton comentarios
        binding.btnComentarios.setOnClickListener{
            val intent = Intent(this, ComentariosBarberoActivity::class.java)
            intent.putExtra("barberId", barberId)
            startActivity(intent)
        }
    }

    private fun getBarbero() {

        db.collection("usuario")
            .document(barberId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    binding.tvBarberiaName.text = userData?.get("barberiaNombre").toString()
                    binding.tvLocation.text = userData?.get("direccion").toString()
                    binding.tvPhone.text = userData?.get("telefono").toString()
                    val imagelurl = userData?.get("urlProfilePhoto").toString()
                    // Procesa los datos del usuario según sea necesario


                    Glide.with(this)
                        .load(imagelurl)
                        .into(binding.imageView1)

                } else {
                    Log.i("0000000000000", "NO EXISTE ")
                    // El documento no existe
                }
            }
            .addOnFailureListener { exception ->
                // Maneja el error
            }
    }

    private fun getServiciosData() {
        db = FirebaseFirestore.getInstance()
        db.collection("servicio").get().addOnSuccessListener { result ->

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                var existeServicio = false
                for (document in result.documents) {
                    if (document.getString("userBarbero") == barberId.toString()) {
                        existeServicio = true
                        Log.i("00000000", "SI TIENE")
                    } else {
                        Log.i("00000000", "NOOO TIENE")
                    }
                }
                if (!existeServicio) {
                    binding.btnReservar.isEnabled = false
                }
            }
        }
    }

    private fun getPhotos() {
        db = FirebaseFirestore.getInstance()
        db.collection("photos").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Foto
            val fotos = mutableListOf<PhotoItemVisit>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("userId") == barberId) {
                        val foto = PhotoItemVisit(
                            userId = document.getString("userId") ?: "",
                            photoId = document.getString("photoId") ?: "",
                            urlPhoto = document.getString("urlPhoto") ?: ""
                        )
                        fotos.add(foto)
                    }
                }
                binding.rvFoto.layoutManager = GridLayoutManager(this, 2)
                binding.rvFoto.adapter = PhotoItemVisitAdapter(fotos)
            }
        }
    }

    private fun getComentarios() {
        db = FirebaseFirestore.getInstance()
        db.collection("comentarios").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Reserva
            val comentarios = mutableListOf<ComentarioItem>()


            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("barberoId") == barberId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val comentario = ComentarioItem(
                            comentarioId = document.getString("comentarioId") ?: "",
                            userId = document.getString("userId") ?: "",
                            userName = document.getString("userName") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            comentario = document.getString("comentario") ?: "",
                            servicios = document.getString("servicios") ?: "",
                            puntuacion = document.getString("puntuacion") ?: ""
                        )
                        // Agregar el objeto Barbero a la lista
                        comentarios.add(comentario)
                    }
                }
                if (comentarios.isEmpty()){
                    binding.btnComentarios.isEnabled = false
                } else {
                    val puntuaciones = comentarios.map { comentario ->
                        comentario.puntuacion.toDoubleOrNull() ?: 0.0
                    }

                    val promedioPuntuaciones = if (puntuaciones.isNotEmpty()) {
                        puntuaciones.average()
                    } else {
                        0.0 // o cualquier valor por defecto que prefieras
                    }
                    binding.rbPuntuacion.rating = promedioPuntuaciones.toFloat()
                }
            }
        }
    }

//cliente@gmail.com
}