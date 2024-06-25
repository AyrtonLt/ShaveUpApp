package com.unmsm.shaveupapp.adapterComentario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class ComentarioItemAdapter(
    private val comentarioList: List<ComentarioItem>,
    private val onClickListener: (ComentarioItem) -> Unit
) :
    RecyclerView.Adapter<ComentarioItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ComentarioItemViewHolder(
            layoutInflater.inflate(R.layout.itemcomentario, parent, false)
        )
    }

    override fun getItemCount(): Int = comentarioList.size

    override fun onBindViewHolder(holder: ComentarioItemViewHolder, position: Int) {
        val item = comentarioList[position]
        holder.render(item, onClickListener)
    }
}