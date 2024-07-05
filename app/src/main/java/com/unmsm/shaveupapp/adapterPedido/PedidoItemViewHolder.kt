package com.unmsm.shaveupapp.adapterPedido

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItempedidoBinding

class PedidoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItempedidoBinding.bind(view)

    fun render(pedidoItemModel: PedidoItem, onClickListener: (PedidoItem) -> Unit) {
        binding.pedidoProducto.text = pedidoItemModel.pedidoProducto
        binding.pedidoCantidad.text = pedidoItemModel.pedidoCantidad
        binding.pedidoPrecio.text = pedidoItemModel.pedidoMonto
        binding.pedidoFecha.text = pedidoItemModel.fecha

        itemView.setOnClickListener {
            onClickListener(pedidoItemModel)
        }
    }
}