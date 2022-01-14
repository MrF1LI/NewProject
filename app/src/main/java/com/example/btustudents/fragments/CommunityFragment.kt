package com.example.btustudents.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.AddPostActivity
import com.example.btustudents.PostInfoActivity
import com.example.btustudents.R
import com.example.btustudents.adapters.PostsAdapter
import com.example.btustudents.databinding.FragmentCommunityBinding
import com.example.btustudents.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityFragment: Fragment(R.layout.fragment_community), PostsAdapter.OnItemClickListener {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private val postsDb = FirebaseDatabase.getInstance().getReference("posts")
    private val auth = FirebaseAuth.getInstance()

    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var arrayListPosts: ArrayList<Post>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        loadPosts()

    }

    private fun init() {

        recyclerViewPosts = binding.recyclerViewPosts

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerViewPosts.layoutManager = layoutManager

        arrayListPosts = arrayListOf()

        binding.buttonAddPost.setOnClickListener {
            startActivity(Intent(activity, AddPostActivity::class.java))
        }

    }

    private fun loadPosts() {

        postsDb.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                arrayListPosts.clear()

                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)?: return
                    arrayListPosts.add(post)
                }

                recyclerViewPosts.adapter = PostsAdapter(context!!.applicationContext, arrayListPosts, this@CommunityFragment)

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

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, PostInfoActivity::class.java)
        intent.putExtra("postId", position)
        startActivity(intent)
    }

    override fun onReactClick(position: Int, view: ImageView) {
        view.setImageResource(R.drawable.ic_reacted)
    }

}



















