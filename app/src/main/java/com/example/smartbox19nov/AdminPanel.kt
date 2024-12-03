package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        val manageUsersButton: Button = findViewById(R.id.manageUsersButton)
        val viewLogsButton: Button = findViewById(R.id.viewLogsButton)
        val viewMoreButton: Button = findViewById(R.id.showMoreButton)
        val controlSystemButton: Button = findViewById(R.id.controlSystemButton)

        manageUsersButton.setOnClickListener {
            startActivity(Intent(this, ManageUsersActivity::class.java))
        }

        viewMoreButton.setOnClickListener {
            startActivity(Intent(this, SuperAdminActivity::class.java))
        }


        viewLogsButton.setOnClickListener {
            startActivity(Intent(this, ViewLogsActivity::class.java))
        }


        controlSystemButton.setOnClickListener {
            startActivity(Intent(this, ViewSystemSettings::class.java))
        }
    }
}

