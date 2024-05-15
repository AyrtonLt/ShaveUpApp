package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterServicios.ServicioClienteAdapter
import com.unmsm.shaveupapp.adapterServicios.ServicioItem
import com.unmsm.shaveupapp.adapterServicios.ServicioItemAdapter
import com.unmsm.shaveupapp.databinding.ActivityBarberoProfileBinding
import com.unmsm.shaveupapp.databinding.ActivityMakeReservationBinding
import java.util.Calendar

class MakeReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeReservationBinding
    private lateinit var adapter: ServicioClienteAdapter
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var currentDay: Calendar
    private lateinit var datePickerDialog: DatePickerDialog

    private var db = Firebase.firestore
    private lateinit var userId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_reservation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMakeReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras

        userId = bundle!!.getString("userId").toString()

        Log.i("00000000","$userId")
//        binding.tvSubtitle1.text = "USER IDEEEEEEEEEEEE"

        currentDay = Calendar.getInstance()

        binding.tietDate.setOnClickListener(){
            onClickSetDate(binding.tietDate)
        }


        getServiciosData()

    }

    private fun getServiciosData() {
        db = FirebaseFirestore.getInstance()
        db.collection("servicio").get().addOnSuccessListener { result ->
            // Crear una lista para almacenar objetos Barbero
            val servicios = mutableListOf<ServicioItem>()

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("userBarbero") == userId.toString()) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val servicio = ServicioItem(
                            nombreServicio = document.getString("name") ?: "",
                            descripcionServicio = document.getString("desc") ?: "",
                            precioServicio = "S/ "+document.getString("price"),
                            isSelected = false
                        )
                        // Agregar el objeto Barbero a la lista
                        servicios.add(servicio)
                    }
                }

                Log.i("000000000000","$servicios")
                adapter = ServicioClienteAdapter(servicios)
                binding.rvServicios.setHasFixedSize(true)
                binding.rvServicios.layoutManager = LinearLayoutManager(this)
                binding.rvServicios.adapter = adapter
            }
        }
    }

    private fun onClickSetDate(editText: EditText){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(this, {_, year, month, day ->
            val selectedDate = "$day/${month+1}/$year"
            calendar.set(year,month,day)
            editText.setText(selectedDate)
        }, year, month, day)
        datePickerDialog.datePicker.minDate = currentDay.timeInMillis
        datePickerDialog.show()
    }


}