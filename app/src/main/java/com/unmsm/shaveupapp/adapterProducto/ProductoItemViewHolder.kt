package com.unmsm.shaveupapp.adapterProducto

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unmsm.shaveupapp.databinding.ItemproductoBinding

class ProductoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemproductoBinding.bind(view)

    fun render(productoItemModel: ProductoItem, onClickListener: (ProductoItem) -> Unit) {
        binding.tvProductoName.text = productoItemModel.productoName
        binding.tvProductoInfo.text = productoItemModel.productoInfo
        binding.tvProductoPrice.text = productoItemModel.productoPrice
        Glide.with(binding.ivProductoPhoto.context).load(productoItemModel.productoPhoto)
            .into(binding.ivProductoPhoto)

        itemView.setOnClickListener {
            onClickListener(productoItemModel)
        }
    }
}