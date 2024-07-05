package com.unmsm.shaveupapp.ui.menu.barbero.createProducto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.ActivityEditProductoBinding

class EditProductoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductoBinding

    private var db = Firebase.firestore
    private lateinit var progressDialog: AlertDialog

    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null
    private var productId: String = ""
    private var photoUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        val bundle: Bundle? = intent.extras
        val productoId = bundle!!.getString("productoId").toString()
        productId = productoId

        getProductoData(productoId)

        binding.btnEditProducto.setOnClickListener {

            if (validateInputs()) {
                showProgressDialog()

                if (filePath == null) {
                    val nombreProducto = binding.tietProductoName.text.toString()
                    val infoProducto = binding.tietProductoDesc.text.toString()
                    val priceProducto = binding.tietProductoPrice.text.toString()

                    val updates = hashMapOf<String, Any>(
                        "productoName" to nombreProducto,
                        "productoInfo" to infoProducto,
                        "productoPrice" to priceProducto
                    )

                    val userRef = db.collection("productos").document(productId)

                    //Actualizar
                    userRef.update(updates).addOnSuccessListener {
                        // Éxito al actualizar los datos
                        Toast.makeText(
                            this,
                            "Datos actualizados correctamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        dismissProgressDialog()
                        finish()
                    }
                        .addOnFailureListener { e ->
                            // Error al actualizar los datos
                            Toast.makeText(
                                this,
                                "Error al actualizar los datos: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    val ref = storageReference.child("productoPhoto/$productId")
                    ref.putFile(filePath!!).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val nombreProducto = binding.tietProductoName.text.toString()
                            val infoProducto = binding.tietProductoDesc.text.toString()
                            val priceProducto = binding.tietProductoPrice.text.toString()

                            val updates = hashMapOf<String, Any>(
                                "productoName" to nombreProducto,
                                "productoInfo" to infoProducto,
                                "productoPrice" to priceProducto,
                                "productoPhoto" to uri.toString()
                            )

                            val userRef = db.collection("productos").document(productId)

                            //Actualizar
                            userRef.update(updates).addOnSuccessListener {
                                // Éxito al actualizar los datos
                                Toast.makeText(
                                    this,
                                    "Datos actualizados correctamente",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                dismissProgressDialog()
                                finish()
                            }
                                .addOnFailureListener { e ->
                                    // Error al actualizar los datos
                                    Toast.makeText(
                                        this,
                                        "Error al actualizar los datos: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }
        }
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

        //Validación Nombre
        val nameInput = binding.tietProductoName.text.toString()
        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (nameInput.isEmpty()) {
            binding.tilProductoName.error = "Este campo es obligatorio"
            isValid = false
            return isValid
        } else if (!nameInput.matches(regex)) {
            binding.tilProductoName.error = "Hay caracteres no permitidos"
            isValid = false
            return isValid
        } else {
            binding.tilProductoName.error = null
        }

        //Validación Desc
        val descInput = binding.tietProductoDesc.text.toString()
        val descRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (descInput.isEmpty()) {
            binding.tilProductoDesc.error = "Este campo es obligatorio"
            isValid = false
            return isValid
        } else if (!descInput.matches(descRegex)) {
            binding.tilProductoDesc.error = "Hay caracteres no permitidos"
            isValid = false
            return isValid
        } else {
            binding.tilProductoDesc.error = null
        }

        // Validación Precio
        val priceInput = binding.tietProductoPrice.text.toString()
        val priceRegex = Regex("^\\d+(\\.\\d{1,2})?$")
        if (priceInput.isEmpty()) {
            binding.tilProductoPrice.error = "Este campo es obligatorio"
            isValid = false
            return isValid
        } else if (!priceInput.matches(priceRegex)) {
            binding.tilProductoPrice.error = "Formato de precio no válido"
            isValid = false
            return isValid
        } else {
            binding.tilProductoPrice.error = null
        }

        // Validación maxQuaintoty
        val quantityInput = binding.tietProductoMaxQuantity.text.toString()
        val quantityRegex = Regex("^[1-9]\\d*$")
        if (quantityInput.isEmpty()) {
            binding.tilProductoMaxQuantity.error = "Este campo es obligatorio"
            isValid = false
            return isValid
        } else if (!quantityInput.matches(quantityRegex)) {
            binding.tilProductoMaxQuantity.error = "Solo se permiten números enteros mayores a 0"
            isValid = false
            return isValid
        } else {
            binding.tilProductoMaxQuantity.error = null
        }


        return isValid
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

    private fun getProductoData(productoId: String) {
        val ref = db.collection("productos").document(productoId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val nombreProducto = it.data?.get("productoName").toString()
                val infoProducto = it.data?.get("productoInfo").toString()
                val priceProducto = it.data?.get("productoPrice").toString()
                val maxQuantityProducto = it.data?.get("productoMaxQuantity").toString()

                photoUrl = it.data?.get("productoPhoto").toString()

                binding.tietProductoName.setText(nombreProducto)
                binding.tietProductoDesc.setText(infoProducto)
                binding.tietProductoPrice.setText(priceProducto)
                binding.tietProductoMaxQuantity.setText(maxQuantityProducto)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}