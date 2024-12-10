package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val usernameInput = findViewById<EditText>(R.id.editTextUsername)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val url = "https://sdb-backend.onrender.com/api/v1/auth/login"
        val jsonObject = JSONObject().apply {
            put("email", username)
            put("password", password)
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
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val jsonResponse = JSONObject(it)
                        val status = jsonResponse.optString("status", "ERROR")
                        if (status == "SUCCESS") {
                            val role = jsonResponse.getString("role")
                            runOnUiThread {
                                when (role) {
                                    "Admin" -> {
                                        Toast.makeText(this@LoginActivity, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, AdminPanel::class.java))
                                    }
                                    "Courier" -> {
                                        Toast.makeText(this@LoginActivity, "Welcome Rider!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(Intent(this@LoginActivity, RiderPannelActivity::class.java))
                                        intent.putExtra("email", username)
                                        startActivity(intent)
                                    }
                                    "Customer" -> {
                                        Toast.makeText(this@LoginActivity, "Welcome User!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@LoginActivity, TrackParcelActivity::class.java))
                                        // Redirect to the appropriate customer activity
                                    }
                                    else -> {
                                        Toast.makeText(this@LoginActivity, "Invalid role", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } else {
                            val message = jsonResponse.optString("message", "Invalid credentials")
                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
