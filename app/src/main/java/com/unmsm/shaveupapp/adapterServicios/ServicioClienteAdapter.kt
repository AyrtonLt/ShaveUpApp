package com.unmsm.shaveupapp.adapterServicios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ServicioClienteAdapter(private val servicioList: List<ServicioItem>, private val onItemSelected:(Int) ->Unit)
    :RecyclerView.Adapter<ServicioClienteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioClienteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ServicioClienteViewHolder(
            layoutInflater.inflate(
                R.layout.itemserviciocliente,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return servicioList.size
    }

    override fun onBindViewHolder(holder: ServicioClienteViewHolder, position: Int) {
        val item = servicioList[position]
        holder.render(item, onItemSelected)
    }
}