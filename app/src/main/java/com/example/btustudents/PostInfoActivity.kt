package com.example.btustudents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.adapters.CommentsAdapter
import com.example.btustudents.databinding.ActivityPostInfoBinding
import com.example.btustudents.models.Comment
import com.example.btustudents.models.Post
import com.example.btustudents.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostInfoBinding
    private val dbPosts = FirebaseDatabase.getInstance().getReference("posts")
    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")
    private val auth = FirebaseAuth.getInstance()

    private lateinit var arrayListComments: ArrayList<Comment>
    private lateinit var recyclerViewComments: RecyclerView

    private var currentPostId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityPostInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentPostId = intent.extras!!.getInt("postId", 0)

        init()

        loadCurrentPost(currentPostId)
        loadPostComments(currentPostId)

    }

    private fun init() {

        arrayListComments = arrayListOf()
        recyclerViewComments = binding.recyclerViewComments

        val layoutManager = LinearLayoutManager(this)

        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerViewComments.layoutManager = layoutManager

        binding.buttonAddComment.setOnClickListener {
            if (binding.editTextNewComment.text.isNotEmpty()) {
                addNewComment(currentPostId)
            }
        }

    }

    private fun loadCurrentPost(currentPostId: Int) {

        dbPosts.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentPost = snapshot.child(currentPostId.toString()).getValue(Post::class.java)?: return

                    binding.postOwner.text = currentPost.postOwner
                    binding.postContent.text = currentPost.postContent
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        dbPosts.child(currentPostId.toString()).child("postReacts").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.reactCount.text = "$count Like"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun loadPostComments(currentPostId: Int) {
        val currentPost = currentPostId
        Log.d("Show", currentPostId.toString())

        dbPosts.child(currentPost.toString()).child("postComments").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    /* count */

                    val count = snapshot.childrenCount
                    binding.commentCount.text = "$count Comment"

                    /* load */

                    arrayListComments.clear()

                    for (commentSnapshot in snapshot.children) {
                        val currentComment = commentSnapshot.getValue(Comment::class.java)?: return
                        arrayListComments.add(currentComment)
                    }

                    recyclerViewComments.adapter = CommentsAdapter(applicationContext, arrayListComments)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun addNewComment(currentPostId: Int) {
        dbStudents.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val currentStudent = snapshot.child(auth.currentUser!!.uid).getValue(Student::class.java)?: return
                val newCommentContent = binding.editTextNewComment.text.toString()
                val newCommentOwner = currentStudent.name + " " + currentStudent.surname

                val newComment = Comment(newCommentContent, newCommentOwner, auth.currentUser!!.uid)

                dbPosts.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child(currentPostId.toString()).child("postComments").ref.addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val id = snapshot.childrenCount
                                snapshot.child(id.toString()).ref.setValue(newComment).addOnSuccessListener {
                                    binding.editTextNewComment.text.clear()
                                    binding.editTextNewComment.clearFocus()
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}