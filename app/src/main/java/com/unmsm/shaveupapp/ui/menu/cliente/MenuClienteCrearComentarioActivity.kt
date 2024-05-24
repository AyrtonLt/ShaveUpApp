package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.databinding.ActivityMakeReservationBinding
import com.unmsm.shaveupapp.databinding.ActivityMenuClienteCrearComentarioBinding
import com.unmsm.shaveupapp.ui.login.LoginActivity

class MenuClienteCrearComentarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuClienteCrearComentarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_cliente_crear_comentario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMenuClienteCrearComentarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras


        binding.btnCreateComentario.setOnClickListener{

            val barberId = bundle!!.getString("barberId").toString()
            val userId = bundle.getString("userId").toString()
            val servicios = bundle.getString("servicios").toString()
            val username = LoginActivity.DatosCompartidos.obtenerUserName()
            val comentarioText = binding.tietComentario.text
            val puntuacion = binding.rbPuntuacion.rating.toString()
            val comentario = ComentarioItem("aa56sd46asd",userId,username.toString(),barberId,comentarioText.toString(),servicios,puntuacion)
            Log.i("0000000000000","${comentario}")

            // primero haces validaciones
            // aca metes el comentario en la base de datos si ya existe uno con el mismo id se modifica

        }
    }
}