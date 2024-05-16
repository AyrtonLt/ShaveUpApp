package com.unmsm.shaveupapp.ui.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentSignUpBarberoBinding
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class SignUpBarberoFragment : Fragment() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var _binding: FragmentSignUpBarberoBinding? = null
    private val binding get() = _binding!!

    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBarberoBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
                    filePath = result.data!!.data
                }
            }

        binding.btnChooseImage.setOnClickListener {
            chooseImage()
        }

        binding.btnCreateUser.setOnClickListener {

            if (validateInputs()) {

                val emailInput = binding.tietEmail.text.toString()
                val passwordInput = binding.tietPassword.text.toString()
                val nombreInput = binding.tietFirstName.text.toString()
                val apellidoInput = binding.tietLastName.text.toString()
                val apodoInput = binding.tietNickName.text.toString()
                val telefonoInput = binding.tietTelefono.text.toString()
                val nameBarberiaInput = binding.tietNameBarberia.text.toString()
                val locationInput = binding.tietDireccion.text.toString()
                val districtInput = binding.actvDistrito.text.toString()

                auth.createUserWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                uploadProfilePhotoandSaveUser(
                                    userId,
                                    nombreInput,
                                    apellidoInput,
                                    apodoInput,
                                    telefonoInput,
                                    nameBarberiaInput,
                                    locationInput,
                                    districtInput
                                )
                            } else {
                                showError("Error al obtener el ID del usuario.")
                            }
                        } else {
                            showError("Error al crear el usuario: ${task.exception?.message}")
                        }
                    }

            } else {
                Toast.makeText(requireContext(), "existen errores", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Select Picture")
        pickImageLauncher.launch(chooser)
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Validación Email
        val emailInput = binding.tietEmail.text.toString().trim()
        if (emailInput.isEmpty()) {
            binding.tilEmail.error = "Esta campo es obligatorio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.tilEmail.error = "Email inválido"
        } else {
            binding.tilEmail.error = null
        }

        // Validación Password
        val passwordInput = binding.tietPassword.text.toString().trim()
        if (passwordInput.isEmpty()) {
            binding.tilPassword.error = "La contraseña no puede estar vacía"
            isValid = false
        } else if (passwordInput.length < 6) {
            binding.tilPassword.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        //Validación Nombre
        val firstNameInput = binding.tietFirstName.text.toString().trim()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (firstNameInput.isEmpty()) {
            binding.tilFirstName.error = "Este campo es obligatorio"
            isValid = false
        } else if (!firstNameInput.matches(regex)) {
            binding.tilFirstName.error = "Hay caracteres no permitidos"
            isValid = false
        } else {
            binding.tilFirstName.error = null
        }

        // Validación Apellido
        val lastNameInput = binding.tietLastName.text.toString().trim()
        if (lastNameInput.isEmpty()) {
            binding.tilLastName.error = "El apellido no puede estar vacío"
            isValid = false
        } else if (!lastNameInput.matches(regex)) {
            binding.tilLastName.error = "Hay caracteres no permitidos"
            isValid = false
        } else {
            binding.tilLastName.error = null
        }

        //Validación Apodo
        val nickNameInput = binding.tietNickName.text.toString().trim()
        val regexNickName = "^[a-zA-Z0-9_-]*$".toRegex()
        if (!regexNickName.matches(nickNameInput)) {
            binding.tilNickName.error = "Hay caracteres no permitidos"
            isValid = false
        } else {
            binding.tilNickName.error = null
        }

        // Validacion Telefono
        val phoneInput = binding.tietTelefono.text.toString().trim()
        if (phoneInput.isEmpty()) {
            binding.tietTelefono.error = "Este campo es obligatorio"
            isValid = false
        } else {
            binding.tietTelefono.error = null
        }

        //Validación Barberia
        val barberiaNameInput = binding.tietNameBarberia.text.toString().trim()
        val regexBarberia = "^[a-zA-Z0-9_-]*$".toRegex()
        if (!regexBarberia.matches(barberiaNameInput)) {
            binding.tilNameBarberia.error = "Hay caracteres no permitidos"
            isValid = false
        } else if (barberiaNameInput.isEmpty()) {
            binding.tilNameBarberia.error = "Este campo es obligatorio"
            isValid = false
        } else {
            binding.tilNameBarberia.error = null
        }

        //Validación Direccion
        val locationInput = binding.tietDireccion.text.toString().trim()
        val regexLocation = "^[a-zA-Z0-9_-]*$".toRegex()
        if (!regexLocation.matches(locationInput)) {
            binding.tilDireccion.error = "Hay caracteres no permitidos"
            isValid = false
        } else if (locationInput.isEmpty()) {
            binding.tilDireccion.error = "Este campo es obligatorio"
            isValid = false
        } else {
            binding.tilDireccion.error = null
        }

        //Validación Distrito
        val districtInput = binding.actvDistrito.text.toString().trim()
        if (districtInput.isEmpty()) {
            binding.tilDistrito.error = "Tiene que seleccionar una opción"
            isValid = false
        } else {
            binding.tilDistrito.error = null
        }

        return isValid
    }

    private fun uploadProfilePhotoandSaveUser(
        userId: String,
        firstName: String,
        lastName: String,
        apodo: String,
        telefono: String,
        nameBarberia: String,
        location: String,
        district: String
    ) {
        if (filePath != null) {
            val ref = storageReference.child("profilePhoto/$userId")
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val profilePhotoUrl = uri.toString()
                        saveUserToFirestore(
                            userId, firstName, lastName, apodo,
                            telefono,
                            nameBarberia,
                            location,
                            district, profilePhotoUrl
                        )
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            saveUserToFirestore(
                userId, firstName, lastName, apodo,
                telefono,
                nameBarberia,
                location,
                district, ""
            )
        }
    }

    private fun saveUserToFirestore(
        userId: String,
        firstName: String,
        lastName: String,
        apodo: String,
        telefono: String,
        nameBarberia: String,
        location: String,
        district: String,
        imageUrl: String
    ) {
        val usuario = mutableMapOf<String, Any>()
        usuario["userType"] = "1"
        usuario["user_id"] = userId
        usuario["nombre"] = firstName
        usuario["apellido"] = lastName
        usuario["apodo"] = apodo
        usuario["telefono"] = telefono
        usuario["barberiaNombre"] = nameBarberia
        usuario["direccion"] = location
        usuario["distrito"] = district

        //Sólo si existe foto de perfil seleccionada
        if (!imageUrl.isEmpty()) {
            usuario["urlProfilePhoto"] = imageUrl
        }

        FirebaseFirestore.getInstance().collection("usuario").document(userId)
            .set(usuario)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario creado", Toast.LENGTH_SHORT).show()
                navigateToMenuBarbero()
            }
            .addOnFailureListener { e ->
                showError("Ocurrió un error al guardar el usuario: ${e.message}")
            }
    }

    private fun navigateToMenuBarbero() {
        val intent = Intent(requireContext(), MenuBarberoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}