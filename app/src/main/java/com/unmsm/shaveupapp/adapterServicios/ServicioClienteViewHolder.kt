package com.unmsm.shaveupapp.adapterServicios

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemservicioclienteBinding

class ServicioClienteViewHolder(view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemservicioclienteBinding.bind(view)

    fun render(servicioItemModel: ServicioItem) {
        binding.tvServicioName.text = servicioItemModel.nombreServicio
        binding.tvServicioDescription.text = servicioItemModel.descripcionServicio
        binding.tvServicioPrecio.text = servicioItemModel.precioServicio
    }
}