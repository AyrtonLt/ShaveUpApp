package com.unmsm.shaveupapp.adapterPhoto

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unmsm.shaveupapp.databinding.ItemphotoBinding

class PhotoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemphotoBinding.bind(view)

    fun render(photoItemModel: PhotoItem, onClickListener: (PhotoItem) -> Unit) {
        Glide.with(binding.ivPhoto.context).load(photoItemModel.urlPhoto).into(binding.ivPhoto)

        itemView.setOnClickListener {
            onClickListener(photoItemModel)
        }
    }

}