package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

//class OTP_Generator : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_otp_generator)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}
class OTPGenerator : AppCompatActivity() {

    private var generatedOtp: String? = null // Store the generated OTP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp_generator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Reference views
        val otpTextView: TextView = findViewById(R.id.otpTextView)
        val generateOtpButton: Button = findViewById(R.id.generateOtpButton)
        val inputOtpEditText: EditText = findViewById(R.id.inputOtpEditText)
        val verifyOtpButton: Button = findViewById(R.id.verifyOtpButton)

        // Generate OTP button logic
        generateOtpButton.setOnClickListener {
            generatedOtp = generateOtp(6) // Generate a 6-digit OTP
            otpTextView.text = "Generated OTP: $generatedOtp"
            Toast.makeText(this, "OTP Generated: $generatedOtp", Toast.LENGTH_SHORT).show()
        }

        // Verify OTP button logic
        verifyOtpButton.setOnClickListener {
            val enteredOtp = inputOtpEditText.text.toString()
            if (enteredOtp == generatedOtp) {
                Toast.makeText(this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show()
                val changePage = Intent(this,SmartBoxEmulator::class.java)
                startActivity(changePage)
            } else {
                Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to generate a numeric OTP of a given length
    private fun generateOtp(length: Int): String {
        return (1..length).map { Random.nextInt(0, 10) }.joinToString("")
    }
}