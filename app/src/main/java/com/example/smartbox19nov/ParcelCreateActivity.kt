package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class ParcelCreateActivity : AppCompatActivity() {

    private lateinit var customerSpinner: Spinner
    private lateinit var dimensionsSpinner: Spinner
    private lateinit var destinationSpinner: Spinner
    private lateinit var fragileRadioGroup: RadioGroup
    private lateinit var parcelIdTextView: TextView

    private val client = OkHttpClient()

    // Lists for customers and delivery boxes
    private val customersList = mutableListOf<String>()
    private val customerIdsMap = mutableMapOf<String, String>()

    private val deliveryBoxesList = mutableListOf<String>()
    private val deliveryBoxIdsMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_parcel)

        // Initialize views
        customerSpinner = findViewById(R.id.customerSpinner)
        dimensionsSpinner = findViewById(R.id.dimensionsSpinner)
        destinationSpinner = findViewById(R.id.destinationSpinner)
        fragileRadioGroup = findViewById(R.id.fragileRadioGroup)
        parcelIdTextView = findViewById(R.id.parcelIdTextView)
        val createParcelButton: Button = findViewById(R.id.createParcelButton)

        // Generate unique Parcel ID
        parcelIdTextView.text = generateParcelId()

        // Set up dimensions spinner
        val dimensionsOptions = listOf("SMALL", "MEDIUM", "LARGE")
        val dimensionsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dimensionsOptions)
        dimensionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dimensionsSpinner.adapter = dimensionsAdapter

        // Fetch customers and delivery boxes
        fetchCustomers()
        fetchDeliveryBoxes()

        // Handle create parcel button click
        createParcelButton.setOnClickListener {
            val selectedCustomer = customerSpinner.selectedItem.toString()
            val customerId = customerIdsMap[selectedCustomer]
            val selectedSize = dimensionsSpinner.selectedItem.toString()
            val selectedDestination = destinationSpinner.selectedItem.toString()
            val deliveryBoxId = deliveryBoxIdsMap[selectedDestination]
            val isFragile = fragileRadioGroup.checkedRadioButtonId == R.id.fragileYesRadioButton
            val parcelId = parcelIdTextView.text.toString()

            if (customerId.isNullOrEmpty() || selectedSize.isBlank() || deliveryBoxId.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare data for backend
            val jsonObject = JSONObject().apply {
                put("userId", customerId)
                put("size", selectedSize)
                put("destination", selectedDestination)
                put("isFragile", isFragile.toString())
                put("deliveryBoxId", deliveryBoxId)
                put("parcelId", parcelId) // Include the generated parcelId
            }

            val requestBody = RequestBody.create("application/json".toMediaType(), jsonObject.toString())

            // Make POST request to create-parcel endpoint
            val request = Request.Builder()
                .url("https://sdb-backend.onrender.com/api/v1/create-parcel")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("ParcelCreateActivity", "Failed to create parcel: ${e.message}")
                    runOnUiThread {
                        Toast.makeText(this@ParcelCreateActivity, "Failed to create parcel", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@ParcelCreateActivity, "Parcel created successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ParcelCreateActivity", "Error creating parcel: ${response.code}")
                        runOnUiThread {
                            Toast.makeText(this@ParcelCreateActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    private fun generateParcelId(): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { characters.random() }
            .joinToString("")
    }

    private fun fetchCustomers() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-users" // Backend endpoint for fetching users

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ParcelCreateActivity", "Failed to fetch customers: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ParcelCreateActivity, "Failed to load customers", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseCustomers(it)
                        runOnUiThread {
                            val adapter = ArrayAdapter(this@ParcelCreateActivity, android.R.layout.simple_spinner_item, customersList)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            customerSpinner.adapter = adapter
                        }
                    }
                } else {
                    Log.e("ParcelCreateActivity", "Error fetching customers: ${response.code}")
                }
            }
        })
    }

    private fun parseCustomers(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val usersArray = jsonObject.getJSONArray("users")

            for (i in 0 until usersArray.length()) {
                val userObject = usersArray.getJSONObject(i)
                val role = userObject.getString("role")
                if (role == "Customer") { // Only add users with the role "Customer"
                    val customerName = userObject.getString("name")
                    val customerEmail = userObject.getString("email")
                    val displayName = "$customerName ($customerEmail)"
                    customersList.add(displayName)
                    customerIdsMap[displayName] = customerEmail
                }
            }
        } catch (e: Exception) {
            Log.e("ParcelCreateActivity", "Error parsing customers: ${e.message}")
        }
    }

    private fun fetchDeliveryBoxes() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-delivery-boxes"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ParcelCreateActivity", "Failed to fetch delivery boxes: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ParcelCreateActivity, "Failed to load delivery boxes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseDeliveryBoxes(it)
                        runOnUiThread {
                            val adapter = ArrayAdapter(this@ParcelCreateActivity, android.R.layout.simple_spinner_item, deliveryBoxesList)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            destinationSpinner.adapter = adapter
                        }
                    }
                } else {
                    Log.e("ParcelCreateActivity", "Error fetching delivery boxes: ${response.code}")
                }
            }
        })
    }

    private fun parseDeliveryBoxes(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val boxesArray = jsonObject.getJSONArray("deliveryBoxes")

            for (i in 0 until boxesArray.length()) {
                val boxObject = boxesArray.getJSONObject(i)
                val boxId = boxObject.getString("boxId")
                val address = boxObject.getString("address")
                val displayName = address
                deliveryBoxesList.add(displayName)
                deliveryBoxIdsMap[displayName] = boxId
            }
        } catch (e: Exception) {
            Log.e("ParcelCreateActivity", "Error parsing delivery boxes: ${e.message}")
        }
    }
}
