package com.unmsm.shaveupapp.adapterReservas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ReservaItemAdapter(
    private val reservaList: List<ReservaItem>,
    private val onClickListener: (ReservaItem) -> Unit
) : RecyclerView.Adapter<ReservaItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReservaItemViewHolder(
            layoutInflater.inflate(R.layout.itemreserva, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reservaList.size
    }

    override fun onBindViewHolder(holder: ReservaItemViewHolder, position: Int) {
        val item = reservaList[position]
        holder.render(item, onClickListener)
    }

}