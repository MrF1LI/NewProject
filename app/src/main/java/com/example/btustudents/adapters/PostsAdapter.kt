package com.example.btustudents.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.example.btustudents.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class PostsAdapter(val context: Context, private val posts: List<Post>, private val listener: OnItemClickListener): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private val dbPosts = FirebaseDatabase.getInstance().getReference("posts")
    private val auth = FirebaseAuth.getInstance()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        val postOwner: TextView = view.findViewById(R.id.postOwner)
        val postContent: TextView = view.findViewById(R.id.postContent)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val reactCount: TextView = view.findViewById(R.id.reactCount)
//        val userAvatar: CircleImageView = view.findViewById(R.id.userAvatar)

        private val item: RelativeLayout = view.findViewById(R.id.post)

        init {
            item.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

            item.findViewById<ImageView>(R.id.iconReact).setOnClickListener {
                listener.onReactClick(position, item.findViewById(R.id.iconReact))
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentPost = posts[position]

        holder.postOwner.text = currentPost.postOwner
        holder.postContent.text = currentPost.postContent

        dbPosts.child(position.toString()).child("postComments").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val commentsCount = snapshot.childrenCount
                holder.commentCount.text = "$commentsCount Comment"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        dbPosts.child(position.toString()).child("postReacts").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val reactCount = snapshot.childrenCount
                holder.reactCount.text = "$reactCount Like"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val arrayList = arrayListOf<String>()

        dbPosts.child(position.toString()).child("postReacts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val id = snap.getValue(String::class.java)?: return
                    arrayList.add(id)
                }

                if (arrayList.contains(auth.currentUser!!.uid)) {
                    holder.itemView.findViewById<ImageView>(R.id.iconReact).setImageResource(R.drawable.ic_reacted)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun getItemCount() = posts.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onReactClick(position: Int, view: ImageView)
    }

}