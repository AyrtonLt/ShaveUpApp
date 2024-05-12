package com.unmsm.shaveupapp.ui.menu.cliente.config

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentConfigInfoBinding
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class ConfigInfoFragment : Fragment() {

    private var _binding: FragmentConfigInfoBinding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigInfoBinding.inflate(layoutInflater, container, false)

        getUserData()
        firstNameFocusListener()
        lastNameFocusListener()

        binding.btnUpdateUser.setOnClickListener {
            val validFirstName = binding.tilFirstName.error == null
            val validLastName = binding.tilLastName.error == null

            if (validFirstName && validLastName) {
                val firstName = binding.tietFirstName.text.toString()
                val lastName = binding.tietLastName.text.toString()

                val updateMap = mapOf(
                    "nombre" to firstName,
                    "apellido" to lastName
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

                binding.tietFirstName.setText(firstName)
                binding.tietLastName.setText(lastName)
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

    private fun validLastName(): CharSequence? {
        val lastNameText = binding.tietLastName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")

        if (!lastNameText.matches(regex)) {
            return "El nombre no es válido"
        }
        return null
    }
}