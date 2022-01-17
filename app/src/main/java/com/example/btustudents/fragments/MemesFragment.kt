package com.example.btustudents.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.adapters.MemeAdapter
import com.example.btustudents.databinding.FragmentMemesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MemesFragment: Fragment(R.layout.fragment_memes) {

    private var _binding: FragmentMemesBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance().getReference("memes")
    private val storage = FirebaseStorage.getInstance().getReference("Memes")
    private val auth = FirebaseAuth.getInstance()

    private lateinit var imageUri: Uri

    private lateinit var arrayListMemes: ArrayList<String>
    private lateinit var recyclerViewMemes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadMemes()
    }

    private fun init() {
        arrayListMemes = arrayListOf()
        recyclerViewMemes = binding.recyclerViewMemes

        val layoutManager = GridLayoutManager(activity, 2)
        layoutManager.reverseLayout = true

        recyclerViewMemes.layoutManager = layoutManager
        recyclerViewMemes.adapter = MemeAdapter(requireContext(), arrayListMemes)
    }

    private fun loadMemes() {
        arrayListMemes.add("ANlKeMZbjLQ2yMaKhLii5wQCdbp1")
        arrayListMemes.add("FvAnZ6U7ArVZ5VaR4fTq2lcIDRC2")

        recyclerViewMemes.adapter = MemeAdapter(requireContext(), arrayListMemes)

    }

    private fun uploadImage() {
        val fileName = auth.currentUser!!.uid

        Log.d("GET_LOG", imageUri.toString())

        val storage = FirebaseStorage.getInstance().getReference("ProfilePictures/$fileName")
        storage.putFile(imageUri).addOnSuccessListener {
            Log.d("MY_TAG", "Success")
        }.addOnFailureListener {
            Log.d("MY_TAG", "Failed")
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            imageUri = data?.data!!
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}