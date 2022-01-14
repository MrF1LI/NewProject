package com.example.btustudents.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.models.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CommentsAdapter(val context: Context, private val comments: List<Comment>):
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")
    private val dbPosts = FirebaseDatabase.getInstance().getReference("posts")
    private val auth = FirebaseAuth.getInstance()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val commentOwner: TextView = view.findViewById(R.id.commentOwner)
        val commentContent: TextView = view.findViewById(R.id.commentContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentComment = comments[position]

        holder.commentContent.text = currentComment.commentContent
        holder.commentOwner.text = currentComment.commentOwner
    }

    override fun getItemCount() = comments.size

}