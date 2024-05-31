package com.unmsm.shaveupapp.adapterPhotoVisit

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unmsm.shaveupapp.databinding.ItemphotoBinding

class PhotoItemVisitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemphotoBinding.bind(view)

    fun render(photoItemVisitModel: PhotoItemVisit, onClickListener: (PhotoItemVisit) -> Unit) {
        Glide.with(binding.ivPhoto.context).load(photoItemVisitModel.urlPhoto).into(binding.ivPhoto)

        itemView.setOnClickListener {
            onClickListener(photoItemVisitModel)
        }
    }

}