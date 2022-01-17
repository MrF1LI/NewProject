package com.example.btustudents.fragments

import android.app.Dialog
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.adapters.RateAdapter
import com.example.btustudents.databinding.FragmentRateBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RateFragment: Fragment (R.layout.fragment_rate) {

    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!

    private val dbLecturers = FirebaseDatabase.getInstance().getReference("lecturers")
    private lateinit var arrayListLecturers: ArrayList<String>
    private lateinit var recyclerViewRates: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadRates()
    }

    private fun init() {
        arrayListLecturers = arrayListOf()

        recyclerViewRates = binding.recyclerViewRates

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerViewRates.layoutManager = layoutManager

        binding.imageViewMore.setOnClickListener {
            showMenu(it, R.menu.menu_rate)
        }

    }

    private fun showMenu(v: View, menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {



            false
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    private fun loadRates() {
        dbLecturers.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val currentLecturer = snap.key.toString()
                        arrayListLecturers.add(currentLecturer)
                    }
                    recyclerViewRates.adapter = RateAdapter(context!!.applicationContext, arrayListLecturers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}