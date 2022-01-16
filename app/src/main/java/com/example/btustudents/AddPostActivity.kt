package com.example.btustudents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.example.btustudents.databinding.ActivityAddPostBinding
import com.example.btustudents.fragments.AddTagsBottomSheet
import com.example.btustudents.fragments.OnDataPass
import com.example.btustudents.models.Post
import com.example.btustudents.models.Student
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddPostActivity : AppCompatActivity(), OnDataPass {

    private lateinit var binding: ActivityAddPostBinding
    private val dbPosts = FirebaseDatabase.getInstance().getReference("posts")
    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")
    private val auth = FirebaseAuth.getInstance()

    private var arrayListTags: ArrayList<String> = arrayListOf()

    var id: Long = 0

    override fun onDataPass(data: ArrayList<String>) {
        arrayListTags = data

        for (tag in arrayListTags) {
            val chip = Chip(this)

            chip.text = tag
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                binding.filterTags.removeView(chip)
            }
            binding.filterTags.addView(chip)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        binding.editTextPost.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonAddPost.setOnClickListener {

            if (binding.editTextPost.text.isNotEmpty()) {

                AlertDialog.Builder(this)
                    .setTitle("Post")
                    .setMessage("Public Post?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        addPost()
                        dialog.dismiss()
                    }.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                    .show()
            } else {
                Log.d("Show", "Error")
            }

        }

        binding.buttonAddTags.setOnClickListener {
            val modalBottomSheet = AddTagsBottomSheet()

            modalBottomSheet.show(supportFragmentManager, null)

        }

    }

    private fun addPost() {

        dbPosts.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(postSnapshot: DataSnapshot) {
                id = postSnapshot.childrenCount

                dbStudents.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(studentsSnapshot: DataSnapshot) {

                        val postOwner = studentsSnapshot.child(auth.currentUser!!.uid).getValue(Student::class.java)?: return
                        val postContent = binding.editTextPost.text.toString()
                        val postOwnerId = auth.currentUser!!.uid

                        postSnapshot.child(id.toString()).ref.setValue(Post(postContent, postOwner.name + " " + postOwner.surname, postOwnerId)).addOnSuccessListener {

                            for (tag in arrayListTags) {
                                postSnapshot.child(id.toString()).ref.child("postTags").push().setValue(tag)
                            }

                            finish()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}