package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbox19nov.R.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.mainpage)

        val btnTrackParcel = findViewById<Button>(id.btnTrackParcel)
        val btnLogin = findViewById<Button>(id.btnLogin)
        val btnAdmin = findViewById<Button>(id.btnAdmin)
        val btnRider = findViewById<Button>(id.btnRider)

        btnTrackParcel.setOnClickListener {
            val intent = Intent(this, TrackParcelActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnAdmin.setOnClickListener {
            Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,AdminPanel::class.java)
            startActivity(intent)
        }

        btnRider.setOnClickListener{
            Toast.makeText(this, "Rider Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,RiderPannelActivity::class.java)
            startActivity(intent)
        }



    }
}
