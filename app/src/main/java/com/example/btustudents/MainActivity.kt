package com.example.btustudents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.btustudents.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        showNavigation()
        navigationFunction()
    }

    private fun init() {

        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://students-61271.appspot.com/")

        storageReference.child("ProfilePictures/${auth.currentUser!!.uid}").downloadUrl.addOnSuccessListener { url ->
            Glide.with(this).load(url).into(binding.imageUserAvatar)
        }.addOnFailureListener {
            try {
                throw it
            } catch (exc: Exception) {
                Log.d("GET_LOG", exc.toString())
            }
        }

        binding.imageUserAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }

    private fun showNavigation() {
        binding.buttonBurger.setOnClickListener {
            binding.layoutDrawer.openDrawer(GravityCompat.START)
        }
    }

    private fun navigationFunction() {

        val burgerNavigationView = findViewById<NavigationView>(R.id.sidebarNavigation)
        val controller = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.fragmentCommunity,
            R.id.fragmentChats,
            R.id.fragmentMemes,
            R.id.fragmentRates
        ))

        setupActionBarWithNavController(controller, appBarConfiguration)
        burgerNavigationView.setupWithNavController(controller)

    }

}