package com.unmsm.shaveupapp.adapterReservasPropuestas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ReservaPropuestaItemAdapter(
    private val reservaList: List<ReservaPropuestaItem>,
    private val onAcceptReserva: (ReservaPropuestaItem) -> Unit,
    private val onCancelReserva: (ReservaPropuestaItem) -> Unit
) : RecyclerView.Adapter<ReservaPropuestaItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReservaPropuestaItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReservaPropuestaItemViewHolder(
            layoutInflater.inflate(R.layout.itemreservapropuesta, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reservaList.size
    }

    override fun onBindViewHolder(holder: ReservaPropuestaItemViewHolder, position: Int) {
        val item = reservaList[position]
        holder.render(item, onAcceptReserva, onCancelReserva)
    }

}