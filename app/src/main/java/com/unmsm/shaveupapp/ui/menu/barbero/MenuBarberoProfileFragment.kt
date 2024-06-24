package com.unmsm.shaveupapp.ui.menu.barbero

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapter.BarberoItem
import com.unmsm.shaveupapp.adapter.BarberoItemAdapter
import com.unmsm.shaveupapp.adapterPhoto.PhotoItem
import com.unmsm.shaveupapp.adapterPhoto.PhotoItemAdapter
import com.unmsm.shaveupapp.adapterReservas.ReservaItem
import com.unmsm.shaveupapp.adapterServicios.ServicioItem
import com.unmsm.shaveupapp.adapterServicios.ServicioItemAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuBarberoProfileBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuBarberoProfileFragment : Fragment() {

    private var _binding: FragmentMenuBarberoProfileBinding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    private lateinit var storageReference: StorageReference
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage

    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBarberoProfileBinding.inflate(layoutInflater, container, false)

        getUserData()
        getServiciosData()
        getPhotos()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
                    filePath = result.data!!.data
                    uploadPhoto()
                }
            }

        binding.btnCreateServicio.setOnClickListener {
            findNavController().navigate(R.id.action_menuBarberoProfileFragment_to_createServicioFragment)
        }

        binding.btnUploadPhoto.setOnClickListener {
            chooseImage()
        }

        binding.btnUploadProducto.setOnClickListener {
            findNavController().navigate(R.id.action_menuBarberoProfileFragment_to_createProductoFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getServiciosData()
    }

    private fun getServiciosData() {
        db = FirebaseFirestore.getInstance()
        db.collection("servicio").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Barbero
            val servicios = mutableListOf<ServicioItem>()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("userBarbero") == userId.toString()) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val servicio = ServicioItem(
                            servicioId = document.getString("servicioId") ?: "",
                            nombreServicio = document.getString("name") ?: "",
                            descripcionServicio = document.getString("desc") ?: "",
                            precioServicio = document.getString("price") ?: "",
                            isSelected = false
                        )
                        // Agregar el objeto Barbero a la lista
                        servicios.add(servicio)
                    }
                }
                binding.rvServicio.layoutManager = LinearLayoutManager(requireContext())
                binding.rvServicio.adapter = ServicioItemAdapter(
                    servicios,
                    { ServicioItem -> onClickServicio(ServicioItem) })
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
                val barberiaName = it.data?.get("barberiaNombre")?.toString()
                val phone = it.data?.get("telefono")?.toString()
                val location = it.data?.get("direccion")?.toString()

                binding.tvName.setText(firstName + " " + lastName)
                binding.tvBarberiaName.setText(barberiaName)
                binding.tvPhone.setText(phone)
                binding.tvLocation.setText(location)

                val imagelurl = it.data?.get("urlProfilePhoto").toString()
                // Procesa los datos del usuario según sea necesario


                Glide.with(requireContext())
                    .load(imagelurl)
                    .into(binding.imageView1)


            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotos() {
        db = FirebaseFirestore.getInstance()
        db.collection("photos").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Foto
            val fotos = mutableListOf<PhotoItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("userId") == userId) {
                        val foto = PhotoItem(
                            userId = document.getString("userId") ?: "",
                            photoId = document.getString("photoId") ?: "",
                            urlPhoto = document.getString("urlPhoto") ?: ""
                        )
                        fotos.add(foto)
                    }
                }
                binding.rvFoto.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.rvFoto.adapter = PhotoItemAdapter(fotos,
                    { photoItem -> onClickPhoto(photoItem) })
            }
        }
    }

    private fun onClickServicio(servicioItem: ServicioItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Atención")
        builder.setMessage("Selecciona la acción que desear realizar")
        builder.setPositiveButton("Editar servicio") { _, _ ->
            val intent = Intent(requireContext(), EditServicioActivity::class.java).apply {
                putExtra(
                    "servicioId",
                    servicioItem.servicioId
                ) // Reemplaza "clave" por la clave que desees y "valor" por el valor que quieras enviar
            }
            startActivity(intent)

        }
        builder.setNegativeButton("Eliminar") { dialog, which ->

            db = FirebaseFirestore.getInstance()
            db.collection("servicio").document(servicioItem.servicioId).delete()
                .addOnSuccessListener {
                    // Acción del botón 2
                    Toast.makeText(
                        requireContext(),
                        "El servicio ha sido borrada con éxito",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    getServiciosData()

                }.addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error deleting servicio: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }


        }

        builder.setNeutralButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun onClickPhoto(photoItem: PhotoItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Atención")
        builder.setMessage("Selecciona la acción que desear realizar")
        builder.setPositiveButton("Ver foto") { _, _ ->
            val intent = Intent(requireContext(), FullPhotoActivity::class.java).apply {
                putExtra(
                    "urlPhoto",
                    photoItem.urlPhoto
                ) // Reemplaza "clave" por la clave que desees y "valor" por el valor que quieras enviar
            }
            startActivity(intent)

        }
        builder.setNegativeButton("Eliminar") { dialog, which ->

            db = FirebaseFirestore.getInstance()
            db.collection("photos").document(photoItem.photoId).delete().addOnSuccessListener {
                // Acción del botón 2
                Toast.makeText(
                    requireContext(),
                    "La foto ha sido borrada con éxito",
                    Toast.LENGTH_SHORT
                )
                    .show()

                getPhotos()

            }.addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error deleting photo: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }

        builder.setNeutralButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Select Picture")
        pickImageLauncher.launch(chooser)
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun uploadPhoto() {
        showProgressDialog()
        if (filePath != null) {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val date = getCurrentDateTime()
            val ref = storageReference.child("photo/$date")
            ref.putFile(filePath!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val photoUrl = uri.toString()
                    savePhototoFirestore(userId, photoUrl)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePhototoFirestore(userId: String, photoUrl: String) {
        val db = FirebaseFirestore.getInstance()

        // Crear una referencia a la colección a usar
        val collectionRef = db.collection("photos")

        // Crear un nuevo documento con un ID generado automáticamente
        val newDocumentRef = collectionRef.document()

        // Obtener el ID generado automáticamente
        val documentId = newDocumentRef.id


        val foto = PhotoItem(userId, documentId, photoUrl)

        newDocumentRef.set(foto).addOnSuccessListener {
            // Exito
            Toast.makeText(
                requireContext(),
                "Foto subida",
                Toast.LENGTH_LONG
            ).show()
            getPhotos()
            dismissProgressDialog()
        }.addOnFailureListener { e ->
            // Fallo
            println("Error al escribir el documento: $e")
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