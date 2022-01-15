package com.example.btustudents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.btustudents.databinding.ActivityAddPostBinding
import com.example.btustudents.models.Post
import com.example.btustudents.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private val dbPosts = FirebaseDatabase.getInstance().getReference("posts")
    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")
    private val auth = FirebaseAuth.getInstance()

    var id: Long = 0

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
                    .setTitle("Add Post")
                    .setMessage("... Post?")
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