package com.unmsm.shaveupapp.adapterPedido

data class PedidoItem(
    val pedidoId: String,
    val barberoId: String,
    val clienteId: String,
    val productoId: String,
    val pedidoProducto: String,
    val pedidoCantidad: String,
    val pedidoMonto: String,
    val pedidoEstado: String,
    val fecha: String
)
