package com.example.smartbox19nov

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ParcelCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_parcel)

        // Reference the Spinners and RadioGroup
        val dimensionsSpinner: Spinner = findViewById(R.id.dimensionsSpinner)
        val destinationSpinner: Spinner = findViewById(R.id.destinationSpinner)
        val fragileRadioGroup: RadioGroup = findViewById(R.id.fragileRadioGroup)
        val parcelIdTextView: TextView = findViewById(R.id.parcelIdTextView)

        // Generate a random Parcel ID
        val randomParcelId = "PID-${Random.nextInt(100000, 999999)} Auto-generated"
        parcelIdTextView.text = randomParcelId

        // Data for Dimensions Spinner
        val dimensionsOptions = listOf("Select Size", "Small", "Medium", "Large")
        val dimensionsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dimensionsOptions
        )
        dimensionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dimensionsSpinner.adapter = dimensionsAdapter

        // Data for Destination Spinner
        val destinationOptions = listOf("Select Box", "Smart Box 1", "Smart Box 2")
        val destinationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            destinationOptions
        )
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        destinationSpinner.adapter = destinationAdapter

        // Validate selections and create the parcel
        val createParcelButton: Button = findViewById(R.id.createParcelButton)
        createParcelButton.setOnClickListener {
            // Get selected values
            val selectedDimension = dimensionsSpinner.selectedItem.toString()
            val selectedDestination = destinationSpinner.selectedItem.toString()
            val selectedFragileId = fragileRadioGroup.checkedRadioButtonId

            // Validation
            if (selectedDimension == "Select Size") {
                Toast.makeText(this, "Please select a valid size.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDestination == "Select Box") {
                Toast.makeText(this, "Please select a valid destination box.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedFragileId == -1) { // No fragile option selected
                Toast.makeText(this, "Please select if the parcel is fragile.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show success message
            Toast.makeText(this, "Parcel Created Successfully!", Toast.LENGTH_SHORT).show()

            // Navigate back to SuperAdminActivity
            val intent = Intent(this, SuperAdminActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
    }
}
