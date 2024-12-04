package com.example.smartbox19nov

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TrackParcelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trackparcel)

        val trackingIdInput = findViewById<EditText>(R.id.editTextTrackingId)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        btnSearch.setOnClickListener {
            val trackingId = trackingIdInput.text.toString()

            if (trackingId.isNotEmpty()) {
                Toast.makeText(this, "Tracking ID: $trackingId", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a tracking ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
