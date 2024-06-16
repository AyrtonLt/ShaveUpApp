package com.unmsm.shaveupapp.ui.menu.barbero.createServicio

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentCreateServicioBinding
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoProfileBinding
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity

class CreateServicioFragment : Fragment() {

    private var _binding: FragmentCreateServicioBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore

    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateServicioBinding.inflate(layoutInflater, container, false)

        binding.btnCreateServicio.setOnClickListener {
            showProgressDialog()

            if (validateInputs()) {
                // Crear una referencia a la colección a usar
                val collectionRef = db.collection("servicio")

                // Crear un nuevo documento con un ID generado automáticamente
                val newDocumentRef = collectionRef.document()

                // Obtener el ID generado automáticamente
                val documentId = newDocumentRef.id
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val nameServ = binding.tietServicioName.text.toString()
                val descSev = binding.tietServicioDesc.text.toString()
                val priceServ = binding.tietServicioPrice.text.toString()

                val servicio = mutableMapOf<String, Any>()
                servicio["servicioId"] = documentId
                servicio["userBarbero"] = userId
                servicio["name"] = nameServ
                servicio["desc"] = descSev
                servicio["price"] = priceServ

                // Establecer los datos en el documento
                newDocumentRef.set(servicio)
                    .addOnSuccessListener {
                        // Exito
                        Toast.makeText(
                            requireContext(),
                            "Servicio Creado",
                            Toast.LENGTH_LONG
                        ).show()
                        dismissProgressDialog()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener { e ->
                        // Fallo
                        println("Error al escribir el documento: $e")
                    }
            } else {
                Toast.makeText(requireContext(), "Algo salió mal :(", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        }
        return binding.root
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

    private fun validateInputs(): Boolean {
        var isValid = true

        //Validación Nombre
        val nameInput = binding.tietServicioName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (nameInput.isEmpty()) {
            binding.tilServicioName.error = "Este campo es obligatorio"
            isValid = false
        } else if (!nameInput.matches(regex)) {
            binding.tilServicioName.error = "Hay caracteres no permitidos"
            isValid = false
        } else {
            binding.tilServicioName.error = null
        }

        //Validación Desc
        val descInput = binding.tietServicioDesc.text.toString()
        val descRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (descInput.isEmpty()) {
            binding.tilServicioDesc.error = "Este campo es obligatorio"
            isValid = false
        } else if (!descInput.matches(descRegex)) {
            binding.tilServicioDesc.error = "Hay caracteres no permitidos"
            isValid = false
        } else {
            binding.tilServicioDesc.error = null
        }

        // Validación Precio
        val priceInput = binding.tietServicioPrice.text.toString()
        val priceRegex = Regex("^\\d+(\\.\\d{1,2})?$")
        if (priceInput.isEmpty()) {
            binding.tilServicioPrice.error = "Este campo es obligatorio"
            isValid = false
        } else if (!priceInput.matches(priceRegex)) {
            binding.tilServicioPrice.error = "Formato de precio no válido"
            isValid = false
        } else {
            binding.tilServicioPrice.error = null
        }

        return isValid
    }

}