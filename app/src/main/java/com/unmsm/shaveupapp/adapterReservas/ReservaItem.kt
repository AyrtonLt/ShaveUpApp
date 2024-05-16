package com.unmsm.shaveupapp.adapterReservas

data class ReservaItem(
    val reservaId: String,
    val userId: String,
    val barberroId: String,
    val hora: String,
    val fecha: String,
    val serviciosList: List<String>
)
