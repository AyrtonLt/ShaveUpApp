package com.unmsm.shaveupapp.ui.signup

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import androidx.activity.result.ActivityResultLauncher
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

    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpClienteBinding.inflate(layoutInflater, container, false)

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
            showProgressDialog()

            //Hola
            if (validateInputs()) {

                val emailInput = binding.tietEmail.text.toString().trim()
                val passwordInput = binding.tietPassword.text.toString().trim()
                val firstNameInput = binding.tietFirstName.text.toString().trim()
                val lastNameInput = binding.tietLastName.text.toString().trim()

                auth.createUserWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                uploadProfilePhotoandSaveUser(
                                    userId,
                                    firstNameInput,
                                    lastNameInput
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

    // Función para validar los Inputs
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

        return isValid
    }

    private fun uploadProfilePhotoandSaveUser(
        userId: String,
        firstName: String,
        lastName: String
    ) {
        if (filePath != null) {
            val ref = storageReference.child("profilePhoto/$userId")
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val profilePhotoUrl = uri.toString()
                        saveUserToFirestore(userId, firstName, lastName, profilePhotoUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            saveUserToFirestore(userId, firstName, lastName, "")
        }
    }

    private fun saveUserToFirestore(
        userId: String,
        firstName: String,
        lastName: String,
        imageUrl: String
    ) {
        val usuario = mutableMapOf<String, Any>()
        usuario["userType"] = "2"
        usuario["user_id"] = userId
        usuario["nombre"] = firstName
        usuario["apellido"] = lastName

        //Sólo si existe foto de perfil seleccionada
        if (!imageUrl.isEmpty()) {
            usuario["urlProfilePhoto"] = imageUrl
        }

        FirebaseFirestore.getInstance().collection("usuario").document(userId)
            .set(usuario)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Usuario creado correctamente", Toast.LENGTH_LONG)
                    .show()
                dismissProgressDialog()
                navigateToMenuCliente()
            }
            .addOnFailureListener { e ->
                showError("Ocurrió un error al guardar el usuario: ${e.message}")
            }
    }

    private fun navigateToMenuCliente() {
        val intent = Intent(requireContext(), MenuClienteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        // Inflar la vista personalizada
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.progress_dialog, null)

        // Crear el AlertDialog y configurarlo
        progressDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)
            .create()

        progressDialog.show()
    }

    private fun dismissProgressDialog() {
        progressDialog.dismiss()
    }
}