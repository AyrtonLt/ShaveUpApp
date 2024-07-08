package com.unmsm.shaveupapp.ui.login
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterLanguage.LanguageManager
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
        LanguageManager.updateLocale(this, LanguageManager.getSelectedLanguage(this))
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.switchLanguage.isChecked = LanguageManager.getSelectedLanguage(this) == "es"
        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LanguageManager.setSelectedLanguage(this, "es")
            } else {
                LanguageManager.setSelectedLanguage(this, "en")
            }
            recreate() // Recrear la actividad para aplicar el nuevo idioma
        }

        auth = FirebaseAuth.getInstance()
        binding.btnSignIn.setOnClickListener {
            val email = binding.tietEmail.text.toString()
            val password = binding.tietPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.email_password_empty),
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
                                    DatosCompartidos.guardarDatoUserType(userType.toString())
                                    DatosCompartidos.guardarUserName(document.result.getString("nombre").toString())
                                    when (userType) {
                                        "1" -> {
                                            DatosCompartidos.guardarDatoBarberoId(document.result.getString("user_id").toString())
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
                                                getString(R.string.no_type_user),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.no_user_data),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        MaterialAlertDialogBuilder(this).setTitle("Error")
                            .setMessage(getString(R.string.no_registered)).show()
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

        // Actualizar visualmente solo los elementos que cambian con el idioma
        updateTexts()
    }

    private fun updateTexts() {
        binding.switchLanguage.text = getString(R.string.change_language)
        binding.tilEmail.hint = getString(R.string.Email)
        binding.tilPassword.hint = getString(R.string.Password)
        binding.btnSignIn.text = getString(R.string.Login)
        binding.btnSignUp.text = getString(R.string.new_account)
        binding.btnForgotPassword.text = getString(R.string.ForgotPassword)
    }

    //Datos compartidos
    object DatosCompartidos {
        var userType: String? = null
        var miDato2: String? = null
        var userName: String? = null
        fun guardarDatoUserType(dato: String) {
            userType = dato
        }

        fun obtenerDatoUserType(): String? {
            return userType
        }

        fun guardarDatoBarberoId(dato: String) {
            miDato2 = dato
        }

        fun obtenerBarberoId(): String? {
            return miDato2
        }

        fun guardarUserName(dato: String) {
            userName = dato
        }

        fun obtenerUserName(): String? {
            return userName
        }
    }
}
