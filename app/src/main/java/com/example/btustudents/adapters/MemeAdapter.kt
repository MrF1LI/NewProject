package com.example.btustudents.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.R

class MemeAdapter(val context: Context, private val memes: ArrayList<String>):
    RecyclerView.Adapter<MemeAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageViewMeme: ImageView = view.findViewById(R.id.itemMemes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentImage = memes[position]

        holder.imageViewMeme.setImageURI(Uri.parse(currentImage))

    }

    override fun getItemCount() = memes.size

}