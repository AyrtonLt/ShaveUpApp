package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
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
//cliente@gmail.com
}