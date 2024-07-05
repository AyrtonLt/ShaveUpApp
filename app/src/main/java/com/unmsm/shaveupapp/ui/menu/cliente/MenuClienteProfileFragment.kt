package com.unmsm.shaveupapp.ui.menu.cliente

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
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteProfileBinding
import com.unmsm.shaveupapp.ui.menu.barbero.FullPhotoActivity
import com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile.BarberoProfileActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuClienteProfileFragment : Fragment() {

    private var _binding: FragmentMenuClienteProfileBinding? = null
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
        _binding = FragmentMenuClienteProfileBinding.inflate(layoutInflater, container, false)

        getUserData()
        getPhotos()
        getFavBarber()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!.data != null) {
                    filePath = result.data!!.data
                    uploadPhoto()
                }
            }

        binding.btnUploadPhoto.setOnClickListener {
            chooseImage()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getUserData()
        getPhotos()
        getFavBarber()
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, "Select Picture")
        pickImageLauncher.launch(chooser)
    }

    private fun getFavBarber() {
        db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("like").whereEqualTo("clienteId", userId).get()
            .addOnSuccessListener { result ->
                val barberos = mutableListOf<BarberoItem>()

                if (!result.isEmpty) {
                    for (document in result.documents) {
                        val barbero = BarberoItem(
                            userId = document.getString("barberId") ?: "",
                            urlPhoto = document.getString("urlPhotoB") ?: "",
                            barberiaName = document.getString("barberiaName") ?: "",
                            barberoFullName = document.getString("firstName") ?: "",
                            location = document.getString("location") ?: ""
                        )
                        barberos.add(barbero)
                    }
                    binding.rvBarberoFav.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvBarberoFav.adapter = BarberoItemAdapter(barberos,
                        { barberoItem -> onBarberoFav(barberoItem) })
                }
            }
    }

    private fun onBarberoFav(barberoItem: BarberoItem) {
        val intent = Intent(requireContext(), BarberoProfileActivity::class.java)
        intent.putExtra("barberoId", barberoItem.barberoFullName)
        intent.putExtra("userId", barberoItem.userId)
        startActivity(intent)
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("usuario").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val firstName = it.data?.get("nombre")?.toString()
                val lastName = it.data?.get("apellido")?.toString()

                binding.tvName.setText(firstName + " " + lastName)

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