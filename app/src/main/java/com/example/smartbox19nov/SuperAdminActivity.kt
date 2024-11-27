package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SuperAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.super_admin) // Reference to your super_admin.xml layout

        val createParcel: Button = findViewById(R.id.createParcelButton)

        createParcel.setOnClickListener {
            startActivity(Intent(this, ParcelCreateActivity::class.java))
        }
    }
}