package com.unmsm.shaveupapp.adapterServicios

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ServicioItemAdapter(
    private val servicioList: List<ServicioItem>,
    private val onClickListener: (ServicioItem) -> Unit
) :
    RecyclerView.Adapter<ServicioItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ServicioItemViewHolder(
            layoutInflater.inflate(
                R.layout.itemservicio,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return servicioList.size
    }

    override fun onBindViewHolder(holder: ServicioItemViewHolder, position: Int) {
        val item = servicioList[position]
        holder.render(item, onClickListener)
    }
}