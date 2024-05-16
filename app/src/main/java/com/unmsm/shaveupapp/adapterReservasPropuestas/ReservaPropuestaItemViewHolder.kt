package com.unmsm.shaveupapp.adapterReservasPropuestas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemreservaBinding
import com.unmsm.shaveupapp.databinding.ItemreservapropuestaBinding

class ReservaPropuestaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemreservapropuestaBinding.bind(view)

    fun render(
        reservaItemModel: ReservaPropuestaItem,
        onAcceptReserva: (ReservaPropuestaItem) -> Unit,
        onCancelReserva: (ReservaPropuestaItem) -> Unit
    ) {
        binding.tvFecha.text = reservaItemModel.fecha
        binding.tvHora.text = reservaItemModel.hora
        binding.tvDetalle.text = reservaItemModel.serviciosList.joinToString(separator = ", ")

        binding.btnAcceptReserva.setOnClickListener {
            onAcceptReserva(reservaItemModel)
        }

        binding.btnCancelReserva.setOnClickListener {
            onCancelReserva(reservaItemModel)
        }
    }

}