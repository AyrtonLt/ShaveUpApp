package com.unmsm.shaveupapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class BarberoItemAdapter(private val barberoList: List<BarberoItem>) :
    RecyclerView.Adapter<BarberoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BarberoItemViewHolder(
            layoutInflater.inflate(
                R.layout.itembarbero,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return barberoList.size
    }

    override fun onBindViewHolder(holder: BarberoItemViewHolder, position: Int) {
        val item = barberoList[position]
        holder.render(item)
    }
}