package com.unmsm.shaveupapp.adapterProducto

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ProductoItemAdapter(
    private val productoList: List<ProductoItem>,
    private val onClickListener: (ProductoItem) -> Unit
) : RecyclerView.Adapter<ProductoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoItemViewHolder(
            layoutInflater.inflate(R.layout.itemproducto, parent, false)
        )
    }

    override fun getItemCount(): Int = productoList.size

    override fun onBindViewHolder(holder: ProductoItemViewHolder, position: Int) {
        val item = productoList[position]
        holder.render(item, onClickListener)
    }

}