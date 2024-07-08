package com.unmsm.shaveupapp.ui.menu.barbero.config

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
import com.unmsm.shaveupapp.databinding.FragmentConfigInfo2Binding
import com.unmsm.shaveupapp.ui.menu.cliente.config.ConfigInfoFragment

class ConfigInfoFragment : Fragment() {

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var _binding: FragmentConfigInfo2Binding? = null
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
        _binding = FragmentConfigInfo2Binding.inflate(layoutInflater, container, false)

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
                val nombreInput = binding.tietFirstName.text.toString()
                val apellidoInput = binding.tietLastName.text.toString()
                val apodoInput = binding.tietNickName.text.toString()
                val telefonoInput = binding.tietTelefono.text.toString()
                val nameBarberiaInput = binding.tietNameBarberia.text.toString()
                val locationInput = binding.tietDireccion.text.toString()
                val districtInput = binding.actvDistrito.text.toString()

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
                Toast.makeText(requireContext(), getString(R.string.errors_exist), Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        }

        return binding.root
    }


    private fun validateInputs(): Boolean {
        var isValid = true

        //Validación Nombre
        val firstNameInput = binding.tietFirstName.text.toString().trim()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (firstNameInput.isEmpty()) {
            binding.tilFirstName.error = getString(R.string.field_required)
            isValid = false
        } else if (!firstNameInput.matches(regex)) {
            binding.tilFirstName.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilFirstName.error = null
        }

        // Validación Apellido
        val lastNameInput = binding.tietLastName.text.toString().trim()
        if (lastNameInput.isEmpty()) {
            binding.tilLastName.error = getString(R.string.field_required)
            isValid = false
        } else if (!lastNameInput.matches(regex)) {
            binding.tilLastName.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilLastName.error = null
        }

        //Validación Apodo
        val nickNameInput = binding.tietNickName.text.toString().trim()
        val regexNickName = "^[a-zA-Z0-9 _-]*$".toRegex()
        if (!regexNickName.matches(nickNameInput)) {
            binding.tilNickName.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilNickName.error = null
        }

        // Validacion Telefono
        val phoneInput = binding.tietTelefono.text.toString().trim()
        if (phoneInput.isEmpty()) {
            binding.tietTelefono.error = getString(R.string.field_required)
            isValid = false
        } else {
            binding.tietTelefono.error = null
        }

        //Validación Barberia
        val barberiaNameInput = binding.tietNameBarberia.text.toString().trim()
        val regexBarberia = "^[a-zA-Z0-9 _-]*$".toRegex()
        if (!regexBarberia.matches(barberiaNameInput)) {
            binding.tilNameBarberia.error = getString(R.string.field_required)
            isValid = false
        } else if (barberiaNameInput.isEmpty()) {
            binding.tilNameBarberia.error = getString(R.string.field_required)
            isValid = false
        } else {
            binding.tilNameBarberia.error = null
        }

        //Validación Direccion
        val locationInput = binding.tietDireccion.text.toString().trim()
        val regexLocation = "^[a-zA-Z0-9 _-]*$".toRegex()
        if (!regexLocation.matches(locationInput)) {
            binding.tilDireccion.error = getString(R.string.invalid_characters)
            isValid = false
        } else if (locationInput.isEmpty()) {
            binding.tilDireccion.error = getString(R.string.field_required)
            isValid = false
        } else {
            binding.tilDireccion.error = null
        }

        //Validación Distrito
        val districtInput = binding.actvDistrito.text.toString().trim()
        if (districtInput.isEmpty()) {
            binding.tilDistrito.error = getString(R.string.select_option)
            isValid = false
        } else {
            binding.tilDistrito.error = null
        }

        return isValid
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("usuario").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val firstName = it.data?.get("nombre")?.toString()
                val lastName = it.data?.get("apellido")?.toString()
                val nickName = it.data?.get("apodo")?.toString()
                val telephone = it.data?.get("telefono")?.toString()
                val barberiaName = it.data?.get("barberiaNombre")?.toString()
                val location = it.data?.get("direccion")?.toString()
                val districtName = it.data?.get("distrito")?.toString()

                binding.tietFirstName.setText(firstName)
                binding.tietLastName.setText(lastName)
                binding.tietNickName.setText(nickName)
                binding.tietTelefono.setText(telephone)
                binding.tietNameBarberia.setText(barberiaName)
                binding.tietDireccion.setText(location)
                binding.actvDistrito.setText(districtName, false)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show()
        }
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
        lastName: String,
        apodoInput: String,
        telefonoInput: String,
        nameBarberiaInput: String,
        locationInput: String,
        districtInput: String
    ) {
        if (filePath != null) {
            val ref = storageReference.child("profilePhoto/$userId")
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val profilePhotoUrl = uri.toString()
                        saveUserToFirestore(
                            userId,
                            firstName,
                            lastName,
                            apodoInput,
                            telefonoInput,
                            nameBarberiaInput,
                            locationInput,
                            districtInput,
                            profilePhotoUrl
                        )
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), getString(R.string.failed) + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            saveUserToFirestore(
                userId,
                firstName,
                lastName,
                apodoInput,
                telefonoInput,
                nameBarberiaInput,
                locationInput,
                districtInput,
                ""
            )
        }
    }


    private fun saveUserToFirestore(
        userId: String,
        firstName: String,
        lastName: String,
        apodoInput: String,
        telefonoInput: String,
        nameBarberiaInput: String,
        locationInput: String,
        districtInput: String,
        imageUrl: String
    ) {

        val db = FirebaseFirestore.getInstance()



        if (!imageUrl.isEmpty()) {

            // Crea un mapa con los nuevos valores
            val updates = hashMapOf<String, Any>(
                "apellido" to lastName,
                "nombre" to firstName,
                "apodo" to apodoInput,
                "telefono" to telefonoInput,
                "barberiaNombre" to nameBarberiaInput,
                "direccion" to locationInput,
                "distrito" to districtInput,
                "urlProfilePhoto" to imageUrl
            )
            // Referencia al documento específico
            val userRef = db.collection("usuario").document(userId)

            // Actualiza los campos en el documento
            userRef.update(updates)
                .addOnSuccessListener {
                    // Éxito al actualizar los datos
                    Toast.makeText(context, getString(R.string.data_updated_success), Toast.LENGTH_SHORT)
                        .show()
                    dismissProgressDialog()
                }
                .addOnFailureListener { e ->
                    // Error al actualizar los datos
                    Toast.makeText(
                        context,
                        getString(R.string.error_update_data) +"${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            val updates = hashMapOf<String, Any>(
                "apellido" to lastName,
                "nombre" to firstName,
                "apodo" to apodoInput,
                "telefono" to telefonoInput,
                "barberiaNombre" to nameBarberiaInput,
                "direccion" to locationInput,
                "distrito" to districtInput
            )
            // Referencia al documento específico
            val userRef = db.collection("usuario").document(userId)

            // Actualiza los campos en el documento
            userRef.update(updates)
                .addOnSuccessListener {
                    // Éxito al actualizar los datos
                    Toast.makeText(context, getString(R.string.data_updated_success), Toast.LENGTH_SHORT)
                        .show()
                    dismissProgressDialog()
                }

                .addOnFailureListener { e ->
                    // Error al actualizar los datos
                    Toast.makeText(
                        context,
                        getString(R.string.error_update_data) + "${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
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