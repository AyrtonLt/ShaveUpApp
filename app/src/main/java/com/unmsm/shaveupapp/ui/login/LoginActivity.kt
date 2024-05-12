package com.unmsm.shaveupapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.ActivityLoginBinding
import com.unmsm.shaveupapp.ui.forgotpassword.ForgotPasswordActivity
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity
import com.unmsm.shaveupapp.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "El email y la contraseña no pueden estar vacíos",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = auth.currentUser!!.uid

                        FirebaseFirestore.getInstance().collection("usuario").document(userId).get()
                            .addOnCompleteListener { document ->
                                if (document.isSuccessful) {
                                    val userType = document.result.getString("userType")

                                    when (userType) {
                                        "1" -> {
                                            val intent =
                                                Intent(this, MenuBarberoActivity::class.java)
                                            startActivity(intent)
                                        }

                                        "2" -> {
                                            val intent =
                                                Intent(this, MenuClienteActivity::class.java)
                                            startActivity(intent)
                                        }

                                        else -> {
                                            Toast.makeText(
                                                this,
                                                "Tipo de usuario no definido",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error al obtener datos del usuario",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        MaterialAlertDialogBuilder(this).setTitle("Error")
                            .setMessage("Usuario no registrado :(").show()
                    }
                }
            }

        }

        binding.btnForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}