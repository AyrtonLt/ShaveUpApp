package com.unmsm.shaveupapp.ui.menu.cliente


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.adapterPedido.PedidoItem
import com.unmsm.shaveupapp.adapterPedido.PedidoItemAdapter
import com.unmsm.shaveupapp.databinding.FragmentMenuClientePedidosBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MenuClientePedidosFragment : Fragment() {

    private var _binding: FragmentMenuClientePedidosBinding? = null
    private val binding get() = _binding!!
    private var db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClientePedidosBinding.inflate(layoutInflater, container, false)

        getPedidosCreados()
        getPedidosAceptados()
        getPedidosCancelados()
        getPedidosListos()
        getPedidosEntregados()

        return binding.root
    }

    private fun reloadData() {
        getPedidosCreados()
        getPedidosAceptados()
        getPedidosCancelados()
        getPedidosListos()
        getPedidosEntregados()
    }

    private fun getPedidosCreados() {
        db = FirebaseFirestore.getInstance()
        db.collection("pedidos").get().addOnSuccessListener { result ->
            val pedidos = mutableListOf<PedidoItem>()

            if (!result.isEmpty) {
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("pedidoEstado") == "1" && document.getString("clienteId") == userId) {
                        val pedido = PedidoItem(
                            pedidoId = document.getString("pedidoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            clienteId = document.getString("clienteId") ?: "",
                            productoId = document.getString("productoId") ?: "",
                            pedidoProducto = document.getString("pedidoProducto") ?: "",
                            pedidoCantidad = document.getString("pedidoCantidad") ?: "",
                            pedidoMonto = document.getString("pedidoMonto") ?: "",
                            pedidoEstado = document.getString("pedidoEstado") ?: "",
                            fecha = document.getString("fecha") ?: "",
                        )

                        pedidos.add(pedido)
                    }
                }

                binding.rvPedidosSolicitados.layoutManager = LinearLayoutManager(requireContext())
                binding.rvPedidosSolicitados.adapter = PedidoItemAdapter(
                    pedidos,
                    { pedidoCreado -> doNothing(pedidoCreado) }
                )
            }
        }
    }

    private fun getPedidosAceptados() {
        db = FirebaseFirestore.getInstance()
        db.collection("pedidos").get().addOnSuccessListener { result ->
            val pedidos = mutableListOf<PedidoItem>()

            if (!result.isEmpty) {
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("pedidoEstado") == "2" && document.getString("clienteId") == userId) {
                        val pedido = PedidoItem(
                            pedidoId = document.getString("pedidoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            clienteId = document.getString("clienteId") ?: "",
                            productoId = document.getString("productoId") ?: "",
                            pedidoProducto = document.getString("pedidoProducto") ?: "",
                            pedidoCantidad = document.getString("pedidoCantidad") ?: "",
                            pedidoMonto = document.getString("pedidoMonto") ?: "",
                            pedidoEstado = document.getString("pedidoEstado") ?: "",
                            fecha = document.getString("fechaAceptado") ?: "",
                        )

                        pedidos.add(pedido)
                    }
                }

                binding.rvPedidosAceptados.layoutManager = LinearLayoutManager(requireContext())
                binding.rvPedidosAceptados.adapter = PedidoItemAdapter(
                    pedidos,
                    { pedidoCreado -> doNothing(pedidoCreado) }
                )
            }
        }
    }

    private fun getPedidosCancelados() {
        db = FirebaseFirestore.getInstance()
        db.collection("pedidos").get().addOnSuccessListener { result ->
            val pedidos = mutableListOf<PedidoItem>()

            if (!result.isEmpty) {
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("pedidoEstado") == "3" && document.getString("clienteId") == userId) {
                        val pedido = PedidoItem(
                            pedidoId = document.getString("pedidoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            clienteId = document.getString("clienteId") ?: "",
                            productoId = document.getString("productoId") ?: "",
                            pedidoProducto = document.getString("pedidoProducto") ?: "",
                            pedidoCantidad = document.getString("pedidoCantidad") ?: "",
                            pedidoMonto = document.getString("pedidoMonto") ?: "",
                            pedidoEstado = document.getString("pedidoEstado") ?: "",
                            fecha = document.getString("fechaCancelado") ?: "",
                        )

                        pedidos.add(pedido)
                    }
                }

                binding.rvPedidosCancelados.layoutManager = LinearLayoutManager(requireContext())
                binding.rvPedidosCancelados.adapter = PedidoItemAdapter(
                    pedidos,
                    { pedidoCreado -> doNothing(pedidoCreado) }
                )
            }
        }
    }

    private fun getPedidosListos() {
        db = FirebaseFirestore.getInstance()
        db.collection("pedidos").get().addOnSuccessListener { result ->
            val pedidos = mutableListOf<PedidoItem>()

            if (!result.isEmpty) {
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("pedidoEstado") == "4" && document.getString("clienteId") == userId) {
                        val pedido = PedidoItem(
                            pedidoId = document.getString("pedidoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            clienteId = document.getString("clienteId") ?: "",
                            productoId = document.getString("productoId") ?: "",
                            pedidoProducto = document.getString("pedidoProducto") ?: "",
                            pedidoCantidad = document.getString("pedidoCantidad") ?: "",
                            pedidoMonto = document.getString("pedidoMonto") ?: "",
                            pedidoEstado = document.getString("pedidoEstado") ?: "",
                            fecha = document.getString("fechaListo") ?: "",
                        )

                        pedidos.add(pedido)
                    }
                }

                binding.rvPedidoListoEntregas.layoutManager = LinearLayoutManager(requireContext())
                binding.rvPedidoListoEntregas.adapter = PedidoItemAdapter(
                    pedidos,
                    { pedidoCreado -> onClickPedidoItemListoSelected(pedidoCreado) }
                )
            }
        }

    }

    private fun getPedidosEntregados() {
        db = FirebaseFirestore.getInstance()
        db.collection("pedidos").get().addOnSuccessListener { result ->
            val pedidos = mutableListOf<PedidoItem>()

            if (!result.isEmpty) {
                for (document in result.documents) {
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    if (document.getString("pedidoEstado") == "5" && document.getString("clienteId") == userId) {
                        val pedido = PedidoItem(
                            pedidoId = document.getString("pedidoId") ?: "",
                            barberoId = document.getString("barberoId") ?: "",
                            clienteId = document.getString("clienteId") ?: "",
                            productoId = document.getString("productoId") ?: "",
                            pedidoProducto = document.getString("pedidoProducto") ?: "",
                            pedidoCantidad = document.getString("pedidoCantidad") ?: "",
                            pedidoMonto = document.getString("pedidoMonto") ?: "",
                            pedidoEstado = document.getString("pedidoEstado") ?: "",
                            fecha = document.getString("fechaEntregado") ?: "",
                        )

                        pedidos.add(pedido)
                    }
                }

                binding.rvPedidoEntregados.layoutManager = LinearLayoutManager(requireContext())
                binding.rvPedidoEntregados.adapter = PedidoItemAdapter(
                    pedidos,
                    { pedidoCreado -> doNothing(pedidoCreado) }
                )
            }
        }
    }

    private fun doNothing(pedidoCreado: PedidoItem) {

    }

    private fun onClickPedidoItemListoSelected(pedidoCreado: PedidoItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Atención")
        builder.setMessage("Selecciona la acción que desear realizar")
        builder.setPositiveButton("Listo para recoger") { dialog, which ->
            db = FirebaseFirestore.getInstance()
            val documentRef = db.collection("pedidos").document(pedidoCreado.pedidoId)

            // Obtener la fecha actual usando Calendar
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            val updates = hashMapOf<String, Any>(
                "pedidoEstado" to "5",   // Actualiza este campo
                "fechaEntregado" to formattedDate       // Agrega este campo
            )

            // Actualiza y agrega los campos en el documento
            documentRef.update(updates)
                .addOnSuccessListener {
                    // La actualización fue exitosa
                    println("Documento actualizado y campo nuevo agregado con éxito")
                    reloadData()
                }
                .addOnFailureListener { e ->
                    // Ocurrió un error
                    println("Error actualizando el documento: $e")
                }

        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}