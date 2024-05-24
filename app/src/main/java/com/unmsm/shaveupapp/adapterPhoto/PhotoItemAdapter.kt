package com.unmsm.shaveupapp.adapterPhoto

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class PhotoItemAdapter(
    private val photoList: List<PhotoItem>,
    private val onClickListener: (PhotoItem) -> Unit
) :
    RecyclerView.Adapter<PhotoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhotoItemViewHolder(
            layoutInflater.inflate(
                R.layout.itemphoto,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        val item = photoList[position]
        holder.render(item, onClickListener)
    }

}