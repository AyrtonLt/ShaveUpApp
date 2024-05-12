package com.unmsm.shaveupapp.ui.menu.barbero.config

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentConfigInfo2Binding

class ConfigInfoFragment : Fragment() {

    private var _binding: FragmentConfigInfo2Binding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigInfo2Binding.inflate(layoutInflater, container, false)

        getUserData()
        firstNameFocusListener()
        lastNameFocusListener()
        telephoneFocusListener()

        binding.btnUpdateUser.setOnClickListener {
            val validFirstName = binding.tilFirstName.error == null
            val validLastName = binding.tilLastName.error == null
            val validTelephone = binding.tilTelefono.error == null

            if (validFirstName && validLastName && validTelephone) {
                val firstName = binding.tietFirstName.text.toString()
                val lastName = binding.tietLastName.text.toString()
                val nickName = binding.tietNickName.text.toString()
                val phone = binding.tietTelefono.text.toString()
                val barberiaName = binding.tietNameBarberia.text.toString()
                val location = binding.tietDireccion.text.toString()
                val district = binding.actvDistrito.text.toString()

                val updateMap = mapOf(
                    "nombre" to firstName,
                    "apellido" to lastName,
                    "telefono" to phone,
                    "apodo" to nickName,
                    "barberiaNombre" to barberiaName,
                    "direccion" to location,
                    "distrito" to district
                )
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                db.collection("usuario").document(userId).update(updateMap)

                Toast.makeText(requireContext(), "Información Actualizada!", Toast.LENGTH_SHORT)
                    .show()

                parentFragmentManager.popBackStack()

            } else {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage("Existen errores").show()
            }
        }

        return binding.root
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
                //binding.actvDistrito.setText(districtName)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
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