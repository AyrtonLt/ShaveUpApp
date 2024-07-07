package com.unmsm.shaveupapp.adapterComentario

data class ComentarioItem(
    val comentarioId:  String,
    val userId: String,
    val userName: String,
    val barberoId: String,
    val comentario: String,
    val servicios: String,
    val puntuacion: String,
    val photoUrl: String,
    val fecha: String
)
