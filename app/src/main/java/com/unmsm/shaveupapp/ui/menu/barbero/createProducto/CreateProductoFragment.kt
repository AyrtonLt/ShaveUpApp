package com.unmsm.shaveupapp.ui.menu.barbero.createProducto

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterProducto.ProductoItem
import com.unmsm.shaveupapp.databinding.FragmentCreateProductoBinding

class CreateProductoFragment : Fragment() {

    private var _binding: FragmentCreateProductoBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private lateinit var progressDialog: AlertDialog

    private lateinit var storage: FirebaseStorage
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var storageReference: StorageReference
    private var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProductoBinding.inflate(layoutInflater, container, false)

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

        binding.btnCreateProducto.setOnClickListener {


            if (validateInputs()) {
                showProgressDialog()
                // Crear una referencia a la colección a usar
                val collectionRef = db.collection("productos")

                // Crear un nuevo documento con un ID generado automáticamente
                val newDocumentRef = collectionRef.document()

                // Obtener el ID generado automáticamente
                val documentId = newDocumentRef.id

                val ref = storageReference.child("productoPhoto/$documentId")
                ref.putFile(filePath!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val productoPhotoUrl = uri.toString()

                        val barberoId = FirebaseAuth.getInstance().currentUser!!.uid
                        val productoName = binding.tietProductoName.text.toString()
                        val productoInfo = binding.tietProductoDesc.text.toString()
                        val productoPrice = binding.tietProductoPrice.text.toString()
                        val productoMaxQuantity = binding.tietProductoMaxQuantity.text.toString()

                        val producto = ProductoItem(
                            documentId,
                            barberoId,
                            productoName,
                            productoInfo,
                            productoPrice,
                            productoPhotoUrl,
                            productoMaxQuantity
                        )

                        newDocumentRef.set(producto).addOnSuccessListener {
                            // Exito
                            Toast.makeText(
                                requireContext(),
                                "Producto Creado",
                                Toast.LENGTH_LONG
                            ).show()
                            dismissProgressDialog()
                            parentFragmentManager.popBackStack()
                        }
                    }
                }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), getString(R.string.failed) + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }


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

        // Validación Nombre
        val nameInput = binding.tietProductoName.text.toString().trim()
        val nameRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (nameInput.isEmpty()) {
            binding.tilProductoName.error = getString(R.string.field_required)
            isValid = false
        } else if (!nameInput.matches(nameRegex)) {
            binding.tilProductoName.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilProductoName.error = null
        }

        // Validación Descripción
        val descInput = binding.tietProductoDesc.text.toString().trim()
        val descRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+$")
        if (descInput.isEmpty()) {
            binding.tilProductoDesc.error = getString(R.string.field_required)
            isValid = false
        } else if (!descInput.matches(descRegex)) {
            binding.tilProductoDesc.error = getString(R.string.invalid_characters)
            isValid = false
        } else {
            binding.tilProductoDesc.error = null
        }

        // Validación Precio
        val priceInput = binding.tietProductoPrice.text.toString().trim()
        val priceRegex = Regex("^\\d+(\\.\\d{1,2})?$")
        if (priceInput.isEmpty()) {
            binding.tilProductoPrice.error = getString(R.string.field_required)
            isValid = false
        } else if (!priceInput.matches(priceRegex)) {
            binding.tilProductoPrice.error = getString(R.string.invalid_price_format)
            isValid = false
        } else {
            binding.tilProductoPrice.error = null
        }

        // Validación de la imagen seleccionada
        if (filePath == null) {
            Toast.makeText(requireContext(), getString(R.string.upload_photo_required), Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }


}