package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SuperAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.super_admin) // Reference to your super_admin.xml layout

        // Create Parcel button
        val createParcelButton: Button = findViewById(R.id.createParcelButton)
        createParcelButton.setOnClickListener {
            startActivity(Intent(this, ParcelCreateActivity::class.java))
        }

        // Assign Parcel to Rider button
        val assignParcelButton: Button = findViewById(R.id.assignParcelToRiderButton)
        assignParcelButton.setOnClickListener {
            startActivity(Intent(this, AssignParcelActivity::class.java))
        }
    }
}
