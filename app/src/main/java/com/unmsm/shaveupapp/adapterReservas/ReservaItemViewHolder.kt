package com.unmsm.shaveupapp.adapterReservas

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.unmsm.shaveupapp.databinding.ItemreservaBinding
import com.unmsm.shaveupapp.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

class ReservaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemreservaBinding.bind(view)

    fun render(reservaItemModel: ReservaItem, onClickListener: (ReservaItem) -> Unit) {

        val diasPasados = getDaysDifference(reservaItemModel.fecha)

        Log.i("0000000000000","$diasPasados")

        val userTipe = LoginActivity.DatosCompartidos.obtenerDatoUserType()
        // estado aceptado y usuario es barbero
        if (reservaItemModel.estado == "2" && userTipe == "1"){
            binding.btnTerminarServicio.visibility = View.VISIBLE
            binding.btnHacerComentario.visibility = View.GONE
            binding.btnTerminarServicio.setOnClickListener(){
                onClickListener(reservaItemModel)
            }
        //estado terminado y usuario es cliente y diferencia de dias menor que 1 ya esta comentado
        }else if(reservaItemModel.estado == "4" && userTipe == "2" && diasPasados <= 1){
            binding.btnTerminarServicio.visibility = View.GONE
            binding.btnHacerComentario.visibility = View.VISIBLE
            binding.btnHacerComentario.setOnClickListener(){
                onClickListener(reservaItemModel)
            }
        }else{
            binding.btnHacerComentario.visibility = View.GONE
            binding.btnTerminarServicio.visibility = View.GONE
//            itemView.setOnClickListener {
//                onClickListener(reservaItemModel)
//            }
        }

        binding.tvFecha.text = reservaItemModel.fecha
        binding.tvHora.text = reservaItemModel.hora
        binding.tvDetalle.text = reservaItemModel.serviciosList.joinToString(separator = ", ")

    }

    fun getDaysDifference(dateString: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val targetDate = dateFormat.parse(dateString)
        val currentDate = Date()

        val diffInMillis = targetDate?.time?.minus(currentDate.time) ?: 0
        val diffInDays = (diffInMillis / (24 * 60 * 60 * 1000)).toInt()

        return diffInDays.absoluteValue
    }

}