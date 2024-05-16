package com.unmsm.shaveupapp.ui.menu.cliente.visitingBarberoProfile

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterReservas.ReservaItem
import com.unmsm.shaveupapp.adapterServicios.ServicioClienteAdapter
import com.unmsm.shaveupapp.adapterServicios.ServicioItem
import com.unmsm.shaveupapp.adapterServicios.ServicioItemAdapter
import com.unmsm.shaveupapp.databinding.ActivityBarberoProfileBinding
import com.unmsm.shaveupapp.databinding.ActivityMakeReservationBinding
import com.unmsm.shaveupapp.ui.menu.cliente.MenuClienteActivity
import com.unmsm.shaveupapp.ui.signup.SignUpClienteFragment
import java.util.Calendar

class MakeReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeReservationBinding
    private lateinit var adapter: ServicioClienteAdapter
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var currentDay: Calendar
    private lateinit var datePickerDialog: DatePickerDialog
    private var servicios = mutableListOf<ServicioItem>()

    private var db = Firebase.firestore
    private lateinit var barberId : String
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

        barberId = bundle!!.getString("userId").toString()

        Log.i("00000000","$barberId")
//        binding.tvSubtitle1.text = "USER IDEEEEEEEEEEEE"

        currentDay = Calendar.getInstance()

        binding.tietDate.setOnClickListener(){
            onClickSetDate(binding.tietDate)
        }

        binding.tietTime.setOnClickListener(){
            onClickSetTime(binding.tietTime)
        }


        getServiciosData()

        fechaFocusListener()
        horaFocusListener()

        binding.btnReservar.setOnClickListener(){

            //reservaItem
            //idCliente idBarbero idServicios[] fecha hora
            val serviciosSeleccionados = seleccionados()

            val validList = serviciosSeleccionados.isNotEmpty()
            val validDate = binding.tilDate.error == null
            val validTime = binding.tilTime.error == null

            if (validList && validDate && validTime) {

                val fecha = binding.tietDate.text.toString()
                val hora = binding.tietTime.text.toString()


                if (fecha.isEmpty() || hora.isEmpty() || serviciosSeleccionados.isEmpty() ) {
                    Toast.makeText(
                        this,
                        "Existen campos vacíos",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    //crear objetoreserva
                    val clienteId = FirebaseAuth.getInstance().currentUser!!.uid.toString()
                    val reserva = ReservaItem(
                        "SE CREA AL GUARDARSE",
                        clienteId,
                        barberId,
                        hora,
                        fecha,
                        serviciosSeleccionados
                    )

                    Log.i("0000000000000", "$reserva")

                }
            } else {
                MaterialAlertDialogBuilder(this).setTitle("Error")
                    .setMessage("Existen errores").show()
            }
        }

    }

    private fun getServiciosData() {
        db = FirebaseFirestore.getInstance()
        db.collection("servicio").get().addOnSuccessListener { result ->

            // Verificar si la colección no está vacía
            if (!result.isEmpty) {
                // Iterar sobre los documentos obtenidos
                for (document in result.documents) {
                    if (document.getString("userBarbero") == barberId) {
                        // Crear un nuevo objeto Barbero con los datos del documento
                        val servicio = ServicioItem(
                            servicioId = document.id.toString(),
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
                adapter = ServicioClienteAdapter(servicios) {position ->categoryOnItemSelected(position)}
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

    private fun onClickSetTime(editText: EditText){
        val hours = Calendar.HOUR
        val minutes = Calendar.MINUTE
        val timePicker = TimePickerDialog(this, {_, hour, minute ->
            val formattedHour = if (hour < 10) "0$hour" else hour.toString()
            val formattedMinute = if (minute < 10) "0$minute" else minute.toString()
            val selectedTime = "$formattedHour:$formattedMinute"
            editText.setText(selectedTime)
        }, hours, minutes, true)
        timePicker.show()
    }

    private fun categoryOnItemSelected(position:Int){
        servicios[position].isSelected = !servicios[position].isSelected
        adapter.notifyItemChanged(position)
    }

    private fun seleccionados(): List<String> {
        return servicios.filter { it.isSelected }
            .map { it.servicioId }
    }
    private fun fechaFocusListener() {
        binding.tietDate.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilDate.error = validfecha()
            }
        }
    }

    private fun validfecha(): String? {
        val passwordText = binding.tietDate.text.toString()
        if (passwordText.length < 1) {
            return "Seleccione Fecha"
        }

        return null
    }

    private fun horaFocusListener() {
        binding.tietDate.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.tilDate.error = validhora()
            }
        }
    }

    private fun validhora(): String? {
        val passwordText = binding.tietDate.text.toString()
        if (passwordText.length < 1) {
            return "Seleccione hora"
        }
        return null
    }



}