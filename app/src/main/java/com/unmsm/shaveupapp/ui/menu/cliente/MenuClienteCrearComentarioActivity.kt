package com.unmsm.shaveupapp.ui.menu.cliente

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterComentario.ComentarioItem
import com.unmsm.shaveupapp.databinding.ActivityMenuClienteCrearComentarioBinding
import com.unmsm.shaveupapp.ui.login.LoginActivity

class MenuClienteCrearComentarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuClienteCrearComentarioBinding

    private var filePath: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_cliente_crear_comentario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMenuClienteCrearComentarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras

        validComent()

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

        binding.btnCreateComentario.setOnClickListener{

            val db = FirebaseFirestore.getInstance()

            val validComent = binding.tilComentario.error == null
            val validRating = (binding.rbPuntuacion.rating.toInt()!=0)


            if (validComent && validRating) {

                val comentarioText = binding.tietComentario.text.toString()

                if (comentarioText.isEmpty() || !validRating) {
                    Toast.makeText(
                        this,
                        "Existen campos vacíos",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    // Crear una referencia a la colección a usar
                    val collectionRef = db.collection("comentarios")

                    // Crear un nuevo documento con un ID generado automáticamente
                    val newDocumentRef = collectionRef.document()

                    // Obtener el ID generado automáticamente
                    val documentId = newDocumentRef.id

                    //crear Comentario
                    val reservaId = bundle!!.getString("reservaId").toString()

                    val barberId = bundle.getString("barberId").toString()
                    val userId = bundle.getString("userId").toString()
                    val servicios = bundle.getString("servicios").toString()
                    val username = LoginActivity.DatosCompartidos.obtenerUserName()
                    val comentarioText = binding.tietComentario.text
                    val puntuacion = binding.rbPuntuacion.rating.toString()


                    if (filePath != null) {
                        val ref = storageReference.child("comentarioPhoto/$documentId")
                        ref.putFile(filePath!!)
                            .addOnSuccessListener {
                                ref.downloadUrl.addOnSuccessListener { uri ->
                                    val comentarioPhotoUrl = uri.toString()
                                    //saveUserToFirestore(userId, firstName, lastName, profilePhotoUrl)

                                    val comentario = ComentarioItem(
                                        documentId,
                                        userId,
                                        username.toString(),
                                        barberId,
                                        comentarioText.toString(),
                                        servicios,
                                        puntuacion,
                                        comentarioPhotoUrl)
                                    // Log.i("0000000000000","${comentario}")


                                    // Establecer los datos en el documento
                                    newDocumentRef.set(comentario)
                                        .addOnSuccessListener {
                                            // Exito
                                            Toast.makeText(
                                                this,
                                                "Comentario Creado",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            // Fallo
                                            println("Error al escribir el documento: $e")
                                        }
                                    servicioComentado(reservaId)




                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else {
                        //saveUserToFirestore(userId, firstName, lastName, "")
                        val comentario = ComentarioItem(
                            documentId,
                            userId,
                            username.toString(),
                            barberId,
                            comentarioText.toString(),
                            servicios,
                            puntuacion,
                            "")
                        // Log.i("0000000000000","${comentario}")


                        // Establecer los datos en el documento
                        newDocumentRef.set(comentario)
                            .addOnSuccessListener {
                                // Exito
                                Toast.makeText(
                                    this,
                                    "Comentario Creado",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                // Fallo
                                println("Error al escribir el documento: $e")
                            }
                        servicioComentado(reservaId)
                    }











                }
            } else {
                MaterialAlertDialogBuilder(this).setTitle("Error")
                    .setMessage("Existen errores").show()
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

    private fun validComent(): String? {
        val comentario = binding.tietComentario.text.toString()
        if (comentario.length < 1) {
            return "Escriba un comentario"
        }
        return null
    }

    private fun servicioComentado(reservaId: String){
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("citas").document(reservaId)

        // Actualizar la propiedad "estado"
        documentRef.update("estado", "5")
            .addOnSuccessListener {
                // Exito
                Toast.makeText(
                    this,
                    "Estado de la reserva actualizado",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener { e ->
                // Fallo
                println("Error al actualizar el estado: $e")
            }
    }

}