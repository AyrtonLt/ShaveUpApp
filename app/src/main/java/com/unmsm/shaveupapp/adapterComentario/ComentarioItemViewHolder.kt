package com.unmsm.shaveupapp.adapterComentario

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.unmsm.shaveupapp.databinding.ItemcomentarioBinding
import com.unmsm.shaveupapp.databinding.ItemreservaBinding

class ComentarioItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binding = ItemcomentarioBinding.bind(view)

    fun render(comentarioItem: ComentarioItem, onClickListener: (ComentarioItem) -> Unit) {
        binding.tvUserName.text = comentarioItem.userName
        binding.tvComentario.text = comentarioItem.comentario
        binding.tvServicio.text = comentarioItem.servicios
        binding.tvPuntuacion.text = paintStars(comentarioItem.puntuacion)

        itemView.setOnClickListener {
            onClickListener(comentarioItem)
        }
    }

    fun paintStars(rating: String): String {
        val ratingValue = rating.toDoubleOrNull() ?: return "Valor inválido"
        if (ratingValue < 1 || ratingValue > 5) {
            return "Valor fuera de rango"
        }

        val fullStars = "★".repeat(ratingValue.toInt())
        val partialStar = if (ratingValue % 1 != 0.0) "★" else ""
        val emptyStars = "☆".repeat(5 - ratingValue.toInt() - partialStar.count())

        return fullStars + partialStar + emptyStars
    }

}