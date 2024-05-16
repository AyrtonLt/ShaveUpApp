package com.unmsm.shaveupapp.adapterServicios

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemservicioclienteBinding

class ServicioClienteViewHolder(view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemservicioclienteBinding.bind(view)

    fun render(servicioItem: ServicioItem, onItemSelected: (Int) -> Unit) {
        binding.tvServicioName.text = servicioItem.nombreServicio
        binding.tvServicioDescription.text = servicioItem.descripcionServicio
        binding.tvServicioPrecio.text = servicioItem.precioServicio

        itemView.setOnClickListener(){onItemSelected(layoutPosition)}
        binding.checkBox.isChecked = servicioItem.isSelected
    }
}