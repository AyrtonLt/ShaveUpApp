package com.unmsm.shaveupapp.adapterPedido

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class PedidoItemAdapter(
    private val pedidoList: List<PedidoItem>,
    private val onClickListener: (PedidoItem) -> Unit
) : RecyclerView.Adapter<PedidoItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PedidoItemViewHolder(
            layoutInflater.inflate(R.layout.itempedido, parent, false)
        )
    }

    override fun getItemCount(): Int = pedidoList.size

    override fun onBindViewHolder(holder: PedidoItemViewHolder, position: Int) {
        val item = pedidoList[position]
        holder.render(item, onClickListener)
    }

}