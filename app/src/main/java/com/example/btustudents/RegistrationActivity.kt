package com.example.btustudents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.btustudents.databinding.ActivityRegistrationBinding
import com.example.btustudents.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val auth = FirebaseAuth.getInstance()
    private val studentsDb = FirebaseDatabase.getInstance().getReference("students")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        binding.buttonAuthorization.setOnClickListener {
            finish()
        }

        binding.buttonRegistration.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        saveStudentInfo()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Log.d("Show", "Registration Failed.")
                        Log.d("Show", it.exception.toString())
                    }

                }
        }

    }

    private fun saveStudentInfo() {
        val name = binding.editTextName.text.toString()
        val surname = binding.editTextSurname.text.toString()
        val student = Student(name, surname)

        studentsDb.child(auth.currentUser!!.uid).setValue(student).addOnCompleteListener {
            Log.d("Show", "Student info successfully saved")
        }.addOnFailureListener {
            Log.d("Show", "Failed to save student info")
            Log.d("Show", it.toString())
        }
    }

}