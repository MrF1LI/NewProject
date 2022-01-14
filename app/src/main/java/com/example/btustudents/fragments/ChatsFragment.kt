package com.example.btustudents.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.adapters.ChatsAdapter
import com.example.btustudents.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsFragment: Fragment(R.layout.fragment_chats) {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")
    private val currentStudent = dbStudents.child(auth.currentUser!!.uid)

    private lateinit var arrayListFriends: ArrayList<String>
    private lateinit var recyclerViewChats: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadChats()
    }

    private fun init() {
        arrayListFriends = arrayListOf()
        recyclerViewChats = binding.recyclerViewChats

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerViewChats.layoutManager = layoutManager

        binding.buttonAddFriend.setOnClickListener {
            addFriend()
        }
    }

    private fun loadChats() {
        currentStudent.child("friends").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                arrayListFriends.clear()

                for (friendSnapshot in snapshot.children) {
                    val currentFriend = friendSnapshot.getValue(String::class.java)?: return
                    arrayListFriends.add(currentFriend)
                    Log.d("Show", currentFriend)
                }

                recyclerViewChats.adapter = ChatsAdapter(context!!.applicationContext, arrayListFriends)
                Log.d("Show", arrayListFriends.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addFriend() {
        
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}