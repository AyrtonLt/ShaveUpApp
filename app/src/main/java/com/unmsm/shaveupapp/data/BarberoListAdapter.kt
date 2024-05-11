package com.unmsm.shaveupapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ListbarberosItemBinding

class BarberoListAdapter(private val barberos: ArrayList<Barbero>) :
    RecyclerView.Adapter<BarberoListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListbarberosItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barbero: Barbero) {
            binding.tvNameBarberia.text = barbero.nameBarberia
            binding.tvName.text = barbero.firstNameBarbero
            binding.tvLocation.text = barbero.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListbarberosItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return barberos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(barberos[position])
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}

