package com.unmsm.shaveupapp.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentSignUpClienteBinding
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class SignUpClienteFragment : Fragment() {

    private var _binding: FragmentSignUpClienteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpClienteBinding.inflate(layoutInflater, container, false)

        emailFocusListener()
        passwordFocusListener()
        firstNameFocusListener()
        lastNameFocusListener()

        binding.btnChooseImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Image to Upload"), 0
            )
        }

        binding.btnCreateUser.setOnClickListener {
            val validEmail = binding.tilEmail.error == null
            val validPassword = binding.tilPassword.error == null
            val validFirstName = binding.tilFirstName.error == null
            val validLastName = binding.tilLastName.error == null

            if (validEmail && validPassword && validFirstName && validLastName) {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Bien")
                    .setMessage("Usuario registrado con éxito").show()
                val intent = Intent(requireContext(), MenuClienteActivity::class.java)
                startActivity(intent)
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