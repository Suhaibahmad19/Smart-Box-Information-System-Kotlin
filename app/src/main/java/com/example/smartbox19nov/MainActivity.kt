package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}
