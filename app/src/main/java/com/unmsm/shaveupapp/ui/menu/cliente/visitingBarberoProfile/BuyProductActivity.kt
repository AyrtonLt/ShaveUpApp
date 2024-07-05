package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterPedido.PedidoItem
import com.unmsm.shaveupapp.databinding.ActivityBuyProductBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class BuyProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyProductBinding

    companion object {
        lateinit var auth: FirebaseAuth
    }

    private var db = Firebase.firestore

    private lateinit var progressDialog: AlertDialog

    private var productId: String = ""
    private var productMaxQuantity: String = ""
    private var productBarber: String = ""
    private var barberId: String = ""
    private var productoId: String = ""
    private var monto: String = ""
    private var priceUnit: String = ""
    private var productoName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuyProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val bundle: Bundle? = intent.extras
        val productoId = bundle!!.getString("productoId").toString()
        productId = productoId

        barberId = bundle.getString("barberId").toString()

        getProductoData()

        binding.btnBuyProducto.setOnClickListener {
            if (validateInputs()) {
                showProgressDialog()
                // Crear una referencia a la colección a usar
                val collectionRef = db.collection("pedidos")

                // Crear un nuevo documento con un ID generado automáticamente
                val newDocumentRef = collectionRef.document()

                // Obtener el ID generado automáticamente
                val documentId = newDocumentRef.id

                val userId = auth.currentUser?.uid.toString()

                val quantity = binding.tietProductoQuantity.text.toString()
                val quan = quantity.toInt()

                val priceUnitDouble = priceUnit.toDouble()
                // Realizar la multiplicación
                val montoDouble = priceUnitDouble * quan
                // Convertir el resultado a String
                monto = montoDouble.toString()

                // Obtener la fecha actual usando Calendar
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(calendar.time)


                val pedido = PedidoItem(
                    documentId,
                    barberId,
                    userId,
                    productoId,
                    productoName,
                    quantity,
                    monto,
                    "1",
                    formattedDate
                )

                newDocumentRef.set(pedido).addOnSuccessListener {
                    // Exito
                    Toast.makeText(
                        this,
                        "Pedido Creado",
                        Toast.LENGTH_LONG
                    ).show()
                    dismissProgressDialog()
                    finish()
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        //Validación Maxima cantidad
        val quantityInput = binding.tietProductoQuantity.text.toString()
        val regex = Regex("^[1-9]\\d*$")

        val productMaxQuantitytoInt = productMaxQuantity.toIntOrNull()


        if (productMaxQuantitytoInt != null) {
            if (regex.matches(quantityInput)) {
                val quantity = quantityInput.toInt()
                if (quantity <= productMaxQuantitytoInt) {
                    // El valor es un número natural mayor a 0 y menor o igual a productMaxQuantity

                    isValid = true

                } else {
                    // El valor excede el máximo permitido
                    binding.tietProductoQuantity.error =
                        "El valor no puede ser mayor que $productMaxQuantity"
                    isValid = false
                    return isValid
                }
            } else {
                // El valor no es válido
                binding.tietProductoQuantity.error = "Ingrese un valor natural mayor a 0"
                isValid = false
                return isValid
            }
        } else {
            // El valor de productMaxQuantity no es un número válido
            binding.tietProductoQuantity.error = "El valor máximo permitido no es válido"
            isValid = false
            return isValid
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

    private fun getProductoData() {
        val ref = db.collection("productos").document(productId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                productBarber = it.data?.get("barberoId").toString()
                val nombreProducto = it.data?.get("productoName").toString()
                val infoProducto = it.data?.get("productoInfo").toString()
                val priceProducto = it.data?.get("productoPrice").toString()
                val photoProducto = it.data?.get("productoPhoto").toString()
                productMaxQuantity = it.data?.get("productoMaxQuantity").toString()
                productoId = it.data?.get("productoId").toString()
                priceUnit = it.data?.get("productoPrice").toString()
                productoName = it.data?.get("productoName").toString()

                binding.tvProductoName.setText(nombreProducto)
                binding.tvProductoInfo.setText(infoProducto)
                binding.tvProductoPrice.setText("S/. $priceProducto")
                Glide.with(this)
                    .load(photoProducto)
                    .into(binding.ivProductoPhoto)

            }
        }.addOnFailureListener {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}