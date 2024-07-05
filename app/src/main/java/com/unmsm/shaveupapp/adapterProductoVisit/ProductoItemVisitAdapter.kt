package com.unmsm.shaveupapp.adapterProductoVisit

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ProductoItemVisitAdapter(
    private val productoList: List<ProductoItemVisit>,
    private val onClickListener: (ProductoItemVisit) -> Unit
) : RecyclerView.Adapter<ProductoItemVisitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoItemVisitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductoItemVisitViewHolder(
            layoutInflater.inflate(R.layout.itemproducto, parent, false)
        )
    }

    override fun getItemCount(): Int = productoList.size

    override fun onBindViewHolder(holder: ProductoItemVisitViewHolder, position: Int) {
        val item = productoList[position]
        holder.render(item, onClickListener)
    }

}