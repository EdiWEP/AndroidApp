package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.todo.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    private fun initializeElements() {
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        registerButton = findViewById(R.id.btnRegister)

        registerButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

           if (email.isEmpty() || password.isEmpty()) {
               Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT).show()
           } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error during account creation", Toast.LENGTH_SHORT).show()
                    }
                }
           }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        initializeElements()
    }
}