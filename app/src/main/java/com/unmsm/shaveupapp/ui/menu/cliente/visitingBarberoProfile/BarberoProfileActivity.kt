package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.adapterComentario.ComentarioItemAdapter
import com.unmsm.shaveupapp.adapterPhotoVisit.PhotoItemVisit
import com.unmsm.shaveupapp.adapterPhotoVisit.PhotoItemVisitAdapter
import com.unmsm.shaveupapp.adapterProductoVisit.ProductoItemVisit
import com.unmsm.shaveupapp.adapterProductoVisit.ProductoItemVisitAdapter
import com.unmsm.shaveupapp.databinding.ActivityBarberoProfileBinding
import com.unmsm.shaveupapp.ui.login.LoginActivity
import com.unmsm.shaveupapp.ui.menu.barbero.FullPhotoActivity

class BarberoProfileActivity : AppCompatActivity() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private lateinit var binding: ActivityBarberoProfileBinding

    private var db = Firebase.firestore
    private lateinit var barberId: String
    private lateinit var phoneNumber: String

    private lateinit var location: String
    private lateinit var urlPhotoB: String
    private lateinit var barberiaName: String
    private lateinit var firstName: String
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

        auth = FirebaseAuth.getInstance()

        val bundle: Bundle? = intent.extras
        val nmaeBarbero = bundle!!.getString("barberoId")
        firstName = nmaeBarbero.toString()
        barberId = bundle.getString("userId").toString()
        LoginActivity.DatosCompartidos.guardarDatoBarberoId(barberId)
        binding.tvNombreBarvero.text = nmaeBarbero
        getLike()
        getBarbero()
        getServiciosData()
        getPhotos()
        getComentarios()
        getProductos()

        binding.btnReservar.setOnClickListener {
            val intent = Intent(this, MakeReservationActivity::class.java)
            intent.putExtra("userId", barberId)
            startActivity(intent)
        }
        //boton comentarios
        binding.btnComentarios.setOnClickListener {
            val intent = Intent(this, ComentariosBarberoActivity::class.java)
            intent.putExtra("barberId", barberId)
            startActivity(intent)
        }
        //Botón WhatsApp
        binding.btnWhatsApp.setOnClickListener {
            val intent = Intent(Intent(Intent.ACTION_VIEW))
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber)
            //(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+phonestr+ "&text="+messagestr)); para mandar mensaje
            startActivity(intent)
        }
        //Botón Location
        binding.btnGeo.setOnClickListener {
            val uri = Uri.parse("geo:0,0?q=$location")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        //BOtón Like
        binding.btnLike.setOnClickListener {
            val textBtn = binding.btnLike.text.toString()
            val userId = auth.currentUser?.uid.toString()


            if (textBtn == "Me gusta") {

                // Crear una referencia a la colección a usar
                val collectionRef = db.collection("like")

                // Crear un nuevo documento con un ID generado automáticamente
                val newDocumentRef = collectionRef.document()

                // Obtener el ID generado automáticamente
                val documentId = newDocumentRef.id

                val like = mutableMapOf<String, Any>()
                like["documentId"] = documentId
                like["clienteId"] = userId
                like["barberId"] = barberId
                like["urlPhotoB"] = urlPhotoB
                like["barberiaName"] = barberiaName
                like["firstName"] = firstName
                like["location"] = location

                // Establecer los datos en el documento
                newDocumentRef.set(like)
                    .addOnSuccessListener {
                        // Exito
                        Toast.makeText(
                            this,
                            getString(R.string.barber_added_to_favorites),
                            Toast.LENGTH_LONG
                        ).show()
                        binding.btnLike.setText(getString(R.string.no_like))
                    }
                    .addOnFailureListener { e ->
                        // Fallo
                        println(getString(R.string.error_writing_document) +" $e")
                    }


            } else if (textBtn == "No me gusta") {
                // Crear una referencia a la colección a usar
                val collectionRef = db.collection("like")

                collectionRef.whereEqualTo("barberId", barberId).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            // Eliminar cada documento que coincida
                            document.reference.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.barber_removed_from_favorites),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    binding.btnLike.setText(getString(R.string.like))
                                }
                                .addOnFailureListener { e ->
                                    println(getString(R.string.error_delete_document)+ "$e")
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        println(getString(R.string.error_search_document) +"$e")
                    }
            }

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
                    phoneNumber = userData?.get("telefono").toString()
                    val imagelurl = userData?.get("urlProfilePhoto").toString()
                    // Procesa los datos del usuario según sea necesario

                    val direc = userData?.get("direccion").toString()
                    val district = userData?.get("distrito").toString()
                    location = "$direc, $district"
                    urlPhotoB = imagelurl
                    barberiaName = userData?.get("barberiaNombre").toString()


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

    private fun getLike() {
        db = FirebaseFirestore.getInstance()
        db.collection("like").get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                var existeLike = false
                for (document in result.documents) {
                    if (document.getString("barberId") == barberId.toString()) {
                        existeLike = true
                        Log.i("00000000", "SI TIENE")
                    } else {
                        Log.i("00000000", "NOOO TIENE")
                    }
                }
                if (existeLike) {
                    binding.btnLike.setText(getString(R.string.no_like))
                } else {
                    binding.btnLike.setText(getString(R.string.like))
                }
            }
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
                binding.rvFoto.adapter = PhotoItemVisitAdapter(fotos,
                    { photoItemVisit -> onClickPhoto(photoItemVisit) })
            }
        }
    }

    private fun getProductos() {
        db = FirebaseFirestore.getInstance()
        db.collection("productos").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Foto
            val productos = mutableListOf<ProductoItemVisit>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("barberoId") == barberId) {
                        val producto = ProductoItemVisit(
                            productoId = document.getString("productoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            productoName = document.getString("productoName") ?: "",
                            productoInfo = document.getString("productoInfo") ?: "",
                            productoPrice = document.getString("productoPrice") ?: "",
                            productoPhoto = document.getString("productoPhoto") ?: "",
                            productoMaxQuantity = document.getString("productoMaxQuantity") ?: ""
                        )
                        productos.add(producto)
                    }
                }
                binding.rvProducto.layoutManager = GridLayoutManager(this, 2)
                binding.rvProducto.adapter = ProductoItemVisitAdapter(productos,
                    { productoItemVisit -> onClickProducto(productoItemVisit) })
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
                            puntuacion = document.getString("puntuacion") ?: "",
                            photoUrl = document.getString("photoUrl") ?: ""

                        )
                        // Agregar el objeto Barbero a la lista
                        comentarios.add(comentario)
                    }
                }
                if (comentarios.isEmpty()) {
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

    private fun onClickPhoto(photoItemVisit: PhotoItemVisit) {
        val intent = Intent(this, FullPhotoActivity::class.java).apply {
            putExtra(
                "urlPhoto",
                photoItemVisit.urlPhoto
            ) // Reemplaza "clave" por la clave que desees y "valor" por el valor que quieras enviar
        }
        startActivity(intent)
    }

    private fun onClickProducto(productoItemVisit: ProductoItemVisit) {
        val intent = Intent(this, BuyProductActivity::class.java).apply {
            putExtra(
                "productoId",
                productoItemVisit.productoId
            )
            putExtra(
                "barberId",
                barberId
            )// Reemplaza "clave" por la clave que desees y "valor" por el valor que quieras enviar
        }
        startActivity(intent)
    }
}