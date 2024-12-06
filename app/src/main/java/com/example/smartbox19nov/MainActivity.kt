package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainpage)

        val btnTrackParcel = findViewById<Button>(R.id.btnTrackParcel)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnTrackParcel.setOnClickListener {
            val intent = Intent(this, TrackParcelActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = R.id.editTextUsername.toString()
            val password = R.id.editTextPassword.toString()

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

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}
