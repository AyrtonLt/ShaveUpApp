package com.unmsm.shaveupapp.ui.forgotpassword

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        emailFocusListener()
        binding.btnSendEmail.setOnClickListener {
            val email = binding.tietEmail.text.toString()

            auth.sendPasswordResetEmail(email.trim()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Email enviado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Ocurrió un error :(", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun emailFocusListener() {
        binding.tietEmail.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.tilEmail.error = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.tietEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "Email inválido"
        }
        return null
    }
}