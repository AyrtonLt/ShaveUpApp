package com.unmsm.shaveupapp.ui.menu.cliente.config

import android.app.Activity
import android.app.AlertDialog
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentConfigInfoBinding
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity
import com.unmsm.shaveupapp.ui.signup.SignUpClienteFragment

class ConfigInfoFragment : Fragment() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var _binding: FragmentConfigInfoBinding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigInfoBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()

        getUserData()

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

        binding.btnUpdateUser.setOnClickListener {
            showProgressDialog()

            if (validateInputs()) {
                val userId = auth.currentUser?.uid.toString()
                val firstNameInput = binding.tietFirstName.text.toString().trim()
                val lastNameInput = binding.tietLastName.text.toString().trim()

                uploadProfilePhotoandSaveUser(
                    userId,
                    firstNameInput,
                    lastNameInput
                )

            } else {
                Toast.makeText(requireContext(), "existen errores", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
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

        val db = FirebaseFirestore.getInstance()



        if (!imageUrl.isEmpty()) {

            // Crea un mapa con los nuevos valores
            val updates = hashMapOf<String, Any>(
                "apellido" to lastName,
                "nombre" to firstName,
                "urlProfilePhoto" to imageUrl
            )
            // Referencia al documento específico
            val userRef = db.collection("usuario").document(userId)

            // Actualiza los campos en el documento
            userRef.update(updates)
                .addOnSuccessListener {
                    // Éxito al actualizar los datos
                    Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_SHORT)
                        .show()
                    dismissProgressDialog()
                }
                .addOnFailureListener { e ->
                    // Error al actualizar los datos
                    Toast.makeText(
                        context,
                        "Error al actualizar los datos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            val updates = hashMapOf<String, Any>(
                "apellido" to lastName,
                "nombre" to firstName
            )
            // Referencia al documento específico
            val userRef = db.collection("usuario").document(userId)

            // Actualiza los campos en el documento
            userRef.update(updates)
                .addOnSuccessListener {
                    // Éxito al actualizar los datos
                    Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_SHORT)
                        .show()
                    dismissProgressDialog()
                }

                .addOnFailureListener { e ->
                    // Error al actualizar los datos
                    Toast.makeText(
                        context,
                        "Error al actualizar los datos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("usuario").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val firstName = it.data?.get("nombre")?.toString()
                val lastName = it.data?.get("apellido")?.toString()

                binding.tietFirstName.setText(firstName)
                binding.tietLastName.setText(lastName)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

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