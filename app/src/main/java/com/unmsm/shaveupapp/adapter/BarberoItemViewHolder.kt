package com.unmsm.shaveupapp.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unmsm.shaveupapp.databinding.ItembarberoBinding
import com.unmsm.shaveupapp.ui.menu.barbero.MenuBarberoActivity

class BarberoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItembarberoBinding.bind(view)

    fun render(barberoItemModel: BarberoItem, onClickListener: (BarberoItem) -> Unit) {
        binding.tvBarberiaName.text = barberoItemModel.barberiaName
        binding.tvBarberoFullName.text = barberoItemModel.barberoFullName
        binding.tvLocation.text = barberoItemModel.location
        Glide.with(binding.ivProfilePhoto.context).load(barberoItemModel.urlPhoto)
            .into(binding.ivProfilePhoto)

        itemView.setOnClickListener { onClickListener(barberoItemModel) }
    }
}