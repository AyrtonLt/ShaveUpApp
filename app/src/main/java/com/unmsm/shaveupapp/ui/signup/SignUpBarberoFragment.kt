package com.unmsm.shaveupapp.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentSignUpBarberoBinding
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity

class SignUpBarberoFragment : Fragment() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var _binding: FragmentSignUpBarberoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBarberoBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()
        telephoneFocusListener()

        binding.btnCreateUser.setOnClickListener {
            val validEmail = binding.tilEmail.error == null
            val validPassword = binding.tilPassword.error == null
            val validFirstName = binding.tilFirstName.error == null
            val validLastName = binding.tilLastName.error == null
            val validTelephone = binding.tilTelefono.error == null

            if (validEmail && validPassword && validFirstName && validLastName && validTelephone) {

                val email = binding.tietEmail.text.toString()
                val password = binding.tietPassword.text.toString()
                val nombre = binding.tietFirstName.text.toString()
                val apellido = binding.tietLastName.text.toString()
                val apodo = binding.tietNickName.text.toString()
                val telefono = binding.tietTelefono.text.toString()
                val nameBarberia = binding.tietNameBarberia.text.toString()
                val location = binding.tietDireccion.text.toString()
                val district = binding.actvDistrito.text.toString()

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = auth.currentUser!!.uid
                        val usuario = mutableMapOf<String, Any>()

                        usuario["userType"] = "1"
                        usuario["user_id"] = userId.toString()
                        usuario["email"] = email
                        usuario["password"] = password
                        usuario["nombre"] = nombre
                        usuario["apellido"] = apellido
                        usuario["apodo"] = apodo
                        usuario["telefono"] = telefono
                        usuario["barberiaNombre"] = nameBarberia
                        usuario["direccion"] = location
                        usuario["distrito"] = district


                        FirebaseFirestore.getInstance().collection("usuario").document(userId)
                            .set(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Usuario creado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(requireContext(), MenuBarberoActivity::class.java)
                                startActivity(intent)
                            }.addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Ocurrió un error :(",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                    }
                }
            }
        }
        return binding.root
    }

    private fun emailFocusListener() {
        binding.tietEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilEmail.error = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.tietEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Email inválido"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.tietPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilPassword.error = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.tietPassword.text.toString()
        if (passwordText.length < 6) {
            return "Mínimo 6 dígitos"
        }
        return null
    }

    private fun firstNameFocusListener() {
        binding.tietFirstName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilFirstName.error = validFirstName()
            }
        }
    }

    private fun validFirstName(): String? {
        val firstNameText = binding.tietFirstName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")

        if (!firstNameText.matches(regex)) {
            return "El nombre no es válido"
        }
        return null
    }

    private fun lastNameFocusListener() {
        binding.tietLastName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilLastName.error = validLastName()
            }
        }
    }

    private fun validLastName(): String? {
        val lastNameText = binding.tietLastName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")

        if (!lastNameText.matches(regex)) {
            return "El nombre no es válido"
        }
        return null
    }

    private fun telephoneFocusListener() {
        binding.tietTelefono.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilTelefono.error = validPhone()
            }
        }
    }

    private fun validPhone(): String? {
        val phoneText = binding.tietTelefono.text.toString()
        if (phoneText.length != 9) {
            return "Número inválido"
        }
        return null
    }
}