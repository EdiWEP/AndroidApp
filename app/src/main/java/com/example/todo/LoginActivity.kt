package com.example.todo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todo.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var googleLoginButton: Button
    private lateinit var gotoRegisterButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private fun initializeElements() {
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)
        gotoRegisterButton = findViewById(R.id.btnGoToRegister)
        googleLoginButton = findViewById(R.id.btnGoogleLogin)

        gotoRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        googleLoginButton.setOnClickListener {
            loginWithGoogle()
        }
    }

    private fun loginWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        println("launching")
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == Activity.RESULT_OK) {
            println("result ok")
            println(result.data)
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleTaskResult(task)
        } else {
            println("result not ok")
            println(result.resultCode)
            println(Activity.RESULT_OK)
            println(Activity.RESULT_CANCELED)
            println(Activity.RESULT_FIRST_USER)
        }
    }

    private fun handleGoogleTaskResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Google authentication error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Google authentication error", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("342524595093-agl24qpm6hv10172bkudqf94iqt4mecb.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        initializeElements()
    }
}