package com.unmsm.shaveupapp.ui.menu.cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unmsm.shaveupapp.R
import com.unmsm.shaveupapp.databinding.FragmentMenuClienteProfileBinding

class MenuClienteProfileFragment : Fragment() {

    private var _binding: FragmentMenuClienteProfileBinding? = null
    private val binding get() = _binding!!

    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuClienteProfileBinding.inflate(layoutInflater, container, false)

        getUserData()

        return binding.root
    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = db.collection("usuario").document(userId)
        ref.get().addOnSuccessListener {
            if (it != null) {
                val firstName = it.data?.get("nombre")?.toString()
                val lastName = it.data?.get("apellido")?.toString()

                binding.tvName.setText(firstName + " " + lastName)

                val imagelurl = it.data?.get("urlProfilePhoto").toString()
                // Procesa los datos del usuario según sea necesario


                Glide.with(requireContext())
                    .load(imagelurl)
                    .into(binding.imageView1)

            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
}