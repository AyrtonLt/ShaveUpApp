package com.unmsm.shaveupapp.ui.menu.barbero

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.ActivityEditServicioBinding

class EditServicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditServicioBinding

    private var db = Firebase.firestore

    private lateinit var progressDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditServicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle: Bundle? = intent.extras
        val servicioId = bundle!!.getString("servicioId").toString()
        getServicioData(servicioId)

        binding.btnEditServicio.setOnClickListener {
            showProgressDialog()

            if (validateInputs()) {
                val idServ = servicioId
                val nameServ = binding.tietServicioName.text.toString()
                val descSev = binding.tietServicioDesc.text.toString()
                val priceServ = binding.tietServicioPrice.text.toString()

                saveServiceToFirestore(idServ, nameServ, descSev, priceServ)
            } else {
                Toast.makeText(this, getString(R.string.errors_exist), Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        }
    }

    private fun showProgressDialog() {
        // Inflar la vista personalizada
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.progress_dialog, null)

        // Crear el AlertDialog y configurarlo
        progressDialog = AlertDialog.Builder(this)
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
            binding.tilServicioName.error = getString(R.string.field_required)
            isValid = false
        } else if (!nameInput.matches(regex)) {
            binding.tilServicioName.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilServicioName.error = null
        }

        //Validación Desc
        val descInput = binding.tietServicioDesc.text.toString()
        val descRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (descInput.isEmpty()) {
            binding.tilServicioDesc.error = getString(R.string.field_required)
            isValid = false
        } else if (!descInput.matches(descRegex)) {
            binding.tilServicioDesc.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilServicioDesc.error = null
        }

        // Validación Precio
        val priceInput = binding.tietServicioPrice.text.toString()
        val priceRegex = Regex("^\\d+(\\.\\d{1,2})?$")
        if (priceInput.isEmpty()) {
            binding.tilServicioPrice.error = getString(R.string.field_required)
            isValid = false
        } else if (!priceInput.matches(priceRegex)) {
            binding.tilServicioPrice.error = getString(R.string.invalid_price_format)
            isValid = false
        } else {
            binding.tilServicioPrice.error = null
        }

        return isValid
    }

    private fun saveServiceToFirestore(
        idServ: String,
        nameServ: String,
        descSev: String,
        priceServ: String
    ) {
        // Crea un mapa con los nuevos valores
        val updates = hashMapOf<String, Any>(
            "name" to nameServ,
            "desc" to descSev,
            "price" to priceServ,
        )
        // Referencia al documento específico
        val userRef = db.collection("servicio").document(idServ)

        // Actualiza los campos en el documento
        userRef.update(updates)
            .addOnSuccessListener {
                // Éxito al actualizar los datos
                Toast.makeText(this, getString(R.string.data_updated_success), Toast.LENGTH_SHORT)
                    .show()
                dismissProgressDialog()
                finish()
            }
            .addOnFailureListener { e ->
                // Error al actualizar los datos
                Toast.makeText(
                    this,
                    getString(R.string.error_update_data)+ "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun getServicioData(servicioId: String) {
        val ref = db.collection("servicio").document(servicioId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val nombreServ = it.data?.get("name")?.toString()
                val descServ = it.data?.get("desc")?.toString()
                val priceServ = it.data?.get("price")?.toString()

                binding.tietServicioName.setText(nombreServ)
                binding.tietServicioDesc.setText(descServ)
                binding.tietServicioPrice.setText(priceServ)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }


    }


}