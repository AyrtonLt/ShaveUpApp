package com.unmsm.shaveupapp.adapterReservas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemreservaBinding

class ReservaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemreservaBinding.bind(view)

    fun render(reservaItemModel: ReservaItem, onClickListener: (ReservaItem) -> Unit) {
        binding.tvFecha.text = reservaItemModel.fecha
        binding.tvHora.text = reservaItemModel.hora
        binding.tvDetalle.text = reservaItemModel.serviciosList.joinToString(separator = ", ")

        itemView.setOnClickListener {
            onClickListener(reservaItemModel)
        }
    }

}