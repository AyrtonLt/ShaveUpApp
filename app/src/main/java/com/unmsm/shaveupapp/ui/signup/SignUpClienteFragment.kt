package com.unmsm.shaveupapp.ui.signup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentSignUpClienteBinding
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class SignUpClienteFragment : Fragment() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var _binding: FragmentSignUpClienteBinding? = null
    private val binding get() = _binding!!

    var fileUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpClienteBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                fileUri = it
            }
        }

        binding.btnChooseImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnCreateUser.setOnClickListener {
            val validEmail = binding.tilEmail.error == null
            val validPassword = binding.tilPassword.error == null
            val validFirstName = binding.tilFirstName.error == null
            val validLastName = binding.tilLastName.error == null

            if (validEmail && validPassword && validFirstName && validLastName) {

                val email = binding.tietEmail.text.toString()
                val password = binding.tietPassword.text.toString()
                val firstName = binding.tietFirstName.text.toString()
                val lastName = binding.tietLastName.text.toString()

                if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Existen campos vacíos",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userId = auth.currentUser!!.uid
                            val usuario = mutableMapOf<String, Any>()

                            usuario["userType"] = "2"
                            usuario["user_id"] = userId.toString()
                            usuario["email"] = email
                            usuario["password"] = password
                            usuario["nombre"] = firstName
                            usuario["apellido"] = lastName

                            FirebaseFirestore.getInstance().collection("usuario").document(userId)
                                .set(usuario).addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Usuario creado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent =
                                        Intent(requireContext(), MenuClienteActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
            } else {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage("Existen errores").show()
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

    private fun validLastName(): CharSequence? {
        val lastNameText = binding.tietLastName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")

        if (!lastNameText.matches(regex)) {
            return "El nombre no es válido"
        }
        return null
    }
}