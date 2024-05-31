package com.unmsm.shaveupapp.adapterPhotoVisit

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.R

class PhotoItemVisitAdapter(
    private val photoList: List<PhotoItemVisit>,
    private val onClickListener: (PhotoItemVisit) -> Unit
) :
    RecyclerView.Adapter<PhotoItemVisitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemVisitViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhotoItemVisitViewHolder(
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

    override fun onBindViewHolder(holder: PhotoItemVisitViewHolder, position: Int) {
        val item = photoList[position]
        holder.render(item, onClickListener)
    }
}