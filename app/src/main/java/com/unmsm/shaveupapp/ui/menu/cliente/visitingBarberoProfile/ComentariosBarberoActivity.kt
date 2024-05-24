package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

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
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoCitasFragment

class ComentariosBarberoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuClienteCrearComentarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_comentarios_barbero)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}