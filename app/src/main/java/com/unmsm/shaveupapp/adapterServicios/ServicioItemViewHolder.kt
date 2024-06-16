package com.unmsm.shaveupapp.adapterServicios

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemservicioBinding

class ServicioItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemservicioBinding.bind(view)

    fun render(servicioItemModel: ServicioItem, onClickListener: (ServicioItem) -> Unit) {
        binding.tvServicioName.text = servicioItemModel.nombreServicio
        binding.tvServicioDescription.text = servicioItemModel.descripcionServicio
        binding.tvServicioPrecio.text = servicioItemModel.precioServicio

        itemView.setOnClickListener {
            onClickListener(servicioItemModel) 
        }
    }
}