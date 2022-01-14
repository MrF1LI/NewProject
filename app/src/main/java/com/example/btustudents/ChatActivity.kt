package com.example.btustudents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btustudents.adapters.MessageAdapter
import com.example.btustudents.databinding.ActivityChatBinding
import com.example.btustudents.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var arrayListMessages: ArrayList<Message>

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("chats")

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        Log.d("Show", "name: $name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = auth.currentUser!!.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        init()
        loadMessages()
        sendMessage(senderUid)

        binding.iconBack.setOnClickListener {
            finish()
        }

    }

    private fun init() {
        recyclerViewMessages = binding.recyclerViewMessages

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false

        recyclerViewMessages.layoutManager = layoutManager

        arrayListMessages = arrayListOf()
    }

    private fun loadMessages() {
        db.child(senderRoom!!).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                arrayListMessages.clear()

                for (messageSnapshot in snapshot.children) {
                    val currentMessage = messageSnapshot.getValue(Message::class.java)?: return
                    arrayListMessages.add(currentMessage)
                }

                recyclerViewMessages.adapter = MessageAdapter(this@ChatActivity, arrayListMessages)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun sendMessage(senderUid: String) {

        binding.iconSendMessage.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            val message = Message(messageText, senderUid)

            db.child(senderRoom!!).child("messages").push().setValue(message).addOnSuccessListener {
                db.child(receiverRoom!!).child("messages").push().setValue(message)
            }

            recyclerViewMessages.smoothScrollToPosition(arrayListMessages.size)

            binding.editTextMessage.text.clear()
        }

    }

}