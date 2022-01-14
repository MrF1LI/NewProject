package com.example.btustudents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.btustudents.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

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
        binding.imageUserAvatar.setOnClickListener {
            signOut()
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
            R.id.fragmentMemes
        ))

        setupActionBarWithNavController(controller, appBarConfiguration)
        burgerNavigationView.setupWithNavController(controller)

    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}