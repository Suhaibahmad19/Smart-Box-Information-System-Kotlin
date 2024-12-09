package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class SignupActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var signupButton: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize UI components
        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        roleSpinner = findViewById(R.id.roleSpinner)
        signupButton = findViewById(R.id.buttonSignup)

        // Set roles in the spinner
        val roles = listOf("Customer", "Courier")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        // Handle signup button click
        signupButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleSpinner.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send signup request to backend
            sendSignupRequest(name, email, password, role)
        }
    }

    private fun sendSignupRequest(name: String, email: String, password: String, role: String) {
        val url = "http://10.0.2.2:8080/api/v1/auth/register"

        val jsonObject = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("role", role)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonObject.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SignupActivity", "Signup failed: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@SignupActivity, "Failed to register user", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("SignupActivity", "Signup successful")
                    runOnUiThread {
                        Toast.makeText(this@SignupActivity, "User registered successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the signup activity
                    }
                } else {
                    Log.e("SignupActivity", "Signup error: ${response.code}")
                    val errorBody = response.body?.string()
                    runOnUiThread {
                        Toast.makeText(this@SignupActivity, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
