package com.unmsm.shaveupapp.adapterPhotoVisit

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unmsm.shaveupapp.databinding.ItemphotoBinding

class PhotoItemVisitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemphotoBinding.bind(view)

    fun render(photoItemVisitModel: PhotoItemVisit) {
        Glide.with(binding.ivPhoto.context).load(photoItemVisitModel.urlPhoto).into(binding.ivPhoto)
    }

}