package com.example.btustudents.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.ChatActivity
import com.example.btustudents.R
import com.example.btustudents.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsAdapter(val context: Context, private val chats: List<String>):
    RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    private val dbStudents = FirebaseDatabase.getInstance().getReference("students")

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val chatName: TextView = view.findViewById(R.id.chatName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentChat = chats[position]

        dbStudents.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val currentStudent = snapshot.child(currentChat).getValue(Student::class.java)?: return

                holder.chatName.text = currentStudent.name + " " + currentStudent.surname

                Log.d("Show", "test: $currentStudent")

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("name", currentStudent.name + " " + currentStudent.surname)
                    intent.putExtra("uid", snapshot.child(currentChat).key)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun getItemCount() = chats.size

}