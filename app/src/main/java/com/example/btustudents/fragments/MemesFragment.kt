package com.example.btustudents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.adapters.MemeAdapter
import com.example.btustudents.databinding.FragmentMemesBinding
import com.google.firebase.database.FirebaseDatabase

class MemesFragment: Fragment(R.layout.fragment_memes) {

    private var _binding: FragmentMemesBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance().getReference("memes")

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

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}