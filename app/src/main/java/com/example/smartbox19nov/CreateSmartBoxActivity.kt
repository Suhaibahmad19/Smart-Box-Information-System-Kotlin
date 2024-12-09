package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class CreateSmartBoxActivity : AppCompatActivity() {

    private lateinit var typeSpinner: Spinner
    private lateinit var addressEditText: EditText
    private lateinit var isSecuredRadioGroup: RadioGroup
    private lateinit var statusSpinner: Spinner
    private lateinit var createSmartBoxButton: Button

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_smart_box)

        // Initialize UI components
        typeSpinner = findViewById(R.id.typeSpinner)
        addressEditText = findViewById(R.id.addressEditText)
        isSecuredRadioGroup = findViewById(R.id.isSecuredRadioGroup)
        statusSpinner = findViewById(R.id.statusSpinner)
        createSmartBoxButton = findViewById(R.id.createSmartBoxButton)

        // Set up type spinner
        val typeOptions = listOf("SMALL", "MEDIUM", "LARGE")
        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOptions)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter

        // Set up status spinner
        val statusOptions = listOf("AVAILABLE", "UNAVAILABLE")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        // Handle button click
        createSmartBoxButton.setOnClickListener {
            val selectedType = typeSpinner.selectedItem.toString()
            val address = addressEditText.text.toString()
            val isSecured = isSecuredRadioGroup.checkedRadioButtonId == R.id.isSecuredYesRadioButton
            val selectedStatus = statusSpinner.selectedItem.toString()

            if (address.isBlank()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare JSON object for backend
            val jsonObject = JSONObject().apply {
                put("type", selectedType)
                put("address", address)
                put("isSecured", isSecured)
                put("status", selectedStatus)
            }

            val requestBody = RequestBody.create(
                "application/json".toMediaType(),
                jsonObject.toString()
            )

            val request = Request.Builder()
                .url("https://sdb-backend.onrender.com/api/v1/create-delivery-box") // Replace with actual backend endpoint
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("CreateSmartBox", "Failed to create smart box: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this@CreateSmartBoxActivity, "Failed to create smart box", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@CreateSmartBoxActivity, "Smart Box created successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("CreateSmartBox", "Error creating smart box: ${response.code}")
                        runOnUiThread {
                            Toast.makeText(this@CreateSmartBoxActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}
