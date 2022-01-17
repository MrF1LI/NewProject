package com.example.btustudents.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class RateAdapter(val context: Context, private val lecturers: ArrayList<String>):
    RecyclerView.Adapter<RateAdapter.ViewHolder>() {

    private val dbLecturers = FirebaseDatabase.getInstance().getReference("lecturers")

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val lecturerName: TextView = view.findViewById(R.id.lecturerName)
        val lecturerAvatar: CircleImageView = view.findViewById(R.id.lecturerAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLecturerId = lecturers[position]

        dbLecturers.child(currentLecturerId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.lecturerName.text = snapshot.child("lecturerName").getValue(String::class.java)?: return
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    override fun getItemCount() = lecturers.size
}