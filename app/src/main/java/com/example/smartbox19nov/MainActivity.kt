package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isLoginScreen = true // Track whether we're on the login screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Start with the login layout

        setupLoginScreen() // Initialize login screen functionality
    }

    private fun setupLoginScreen() {
        // Set up login screen
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val switchToSignupButton = findViewById<Button>(R.id.buttonSwitchToSignup)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // TODO: Add login authentication logic
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminPanel::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        switchToSignupButton.setOnClickListener {
            isLoginScreen = false
            setContentView(R.layout.activity_signup) // Switch to signup layout
            setupSignupScreen() // Initialize signup screen functionality
        }
    }

    private fun setupSignupScreen() {
        // Set up signup screen
        val nameInput = findViewById<EditText>(R.id.editTextName)
        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val signupButton = findViewById<Button>(R.id.buttonSignup)
        val switchToLoginButton = findViewById<Button>(R.id.buttonSwitchToLogin)

        signupButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // TODO: Add signup logic
                Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                isLoginScreen = true
                setContentView(R.layout.activity_login) // Switch back to login layout
                setupLoginScreen() // Reinitialize login screen
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        switchToLoginButton.setOnClickListener {
            isLoginScreen = true
            setContentView(R.layout.activity_login) // Switch back to login layout
            setupLoginScreen() // Reinitialize login screen
        }
    }
}
