package com.example.fingerprintstudentverification.ui.home

import User
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fingerprintstudentverification.databinding.FragmentHomeBinding
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnsave: Button = binding.btnSaveStudent
        homeViewModel.text.observe(viewLifecycleOwner) {

        }
        btnsave.setOnClickListener {
            val db=FirebaseDatabase.getInstance()
            val ref=db.reference
            val firstname=binding.txtfname.text
            val lastname=binding.txtlname.text
            val Regno=binding.txtRegno.text
            val coursename=binding.coursename.text
            val fees=binding.fees.text
            val user=User(firstname.toString(),lastname.toString(),Regno.toString(),coursename.toString(),fees.toString())
           firstname?.clear()
            lastname?.clear()
            Regno?.clear()
            coursename?.clear()
            fees?.clear()
            binding.txtfname.requestFocus()
            ref.child("Students").child(user.Regno).setValue(user).addOnCompleteListener {
                Toast.makeText(context?.applicationContext,"Data saved successfully",Toast.LENGTH_LONG).show()

            }
       }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}