package com.unmsm.shaveupapp.adapterServicios

data class ServicioItem(
    val servicioId: String,
    val nombreServicio: String,
    val descripcionServicio: String,
    val precioServicio: String,
    var isSelected: Boolean
)