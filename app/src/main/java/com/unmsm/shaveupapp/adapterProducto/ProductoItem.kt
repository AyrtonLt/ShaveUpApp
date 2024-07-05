package com.unmsm.shaveupapp.adapterProducto

data class ProductoItem(
    val productoId: String,
    val barberoId: String,
    val productoName: String,
    val productoInfo: String,
    val productoPrice: String,
    val productoPhoto: String,
    val productoMaxQuantity: String
)