package com.unmsm.shaveupapp.adapterReservasPropuestas

data class ReservaPropuestaItem(
    val reservaId: String,
    val estado: String,
    val userId: String,
    val barberroId: String,
    val hora: String,
    val fecha: String,
    val serviciosList: List<String>
)
