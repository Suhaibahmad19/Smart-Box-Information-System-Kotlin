package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AssignParcelActivity : AppCompatActivity() {

    private lateinit var riderSpinner: Spinner
    private lateinit var parcelSpinner: Spinner
    private lateinit var assignButton: Button

    private val client = OkHttpClient()
    private val ridersList = mutableListOf<String>()
    private val parcelsList = mutableListOf<String>()
    private val riderIdsMap = mutableMapOf<String, String>()
    private val parcelIdsMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_parcel)

        // Initialize views
        riderSpinner = findViewById(R.id.riderSpinner)
        parcelSpinner = findViewById(R.id.parcelSpinner)
        assignButton = findViewById(R.id.assignButton)

        // Fetch riders and parcels from the backend
        fetchCouriers()
        fetchParcels()

        // Handle assign button click
        assignButton.setOnClickListener {
            assignParcelToRider()
        }
    }

    private fun fetchCouriers() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-users" // Backend endpoint for fetching users

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AssignParcelActivity", "Failed to fetch couriers: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AssignParcelActivity, "Failed to load couriers", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseCouriers(it)
                        runOnUiThread {
                            val adapter = ArrayAdapter(this@AssignParcelActivity, android.R.layout.simple_spinner_item, ridersList)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            riderSpinner.adapter = adapter
                        }
                    }
                } else {
                    Log.e("AssignParcelActivity", "Error fetching couriers: ${response.code}")
                }
            }
        })
    }

    private fun parseCouriers(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val usersArray = jsonObject.getJSONArray("users")

            for (i in 0 until usersArray.length()) {
                val userObject = usersArray.getJSONObject(i)
                val role = userObject.getString("role")
                if (role == "Courier") { // Only add users with the role "Courier"
                    val courierName = userObject.getString("name")
                    val courierId = userObject.getString("email")
                    ridersList.add(courierName)
                    riderIdsMap[courierName] = courierId
                }
            }
        } catch (e: Exception) {
            Log.e("AssignParcelActivity", "Error parsing couriers: ${e.message}")
        }
    }



    private fun fetchParcels() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-parcels" // Replace with the actual backend endpoint for parcels

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AssignParcelActivity", "Failed to fetch parcels: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AssignParcelActivity, "Failed to load parcels", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseParcels(it)
                        runOnUiThread {
                            val adapter = ArrayAdapter(this@AssignParcelActivity, android.R.layout.simple_spinner_item, parcelsList)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            parcelSpinner.adapter = adapter
                        }
                    }
                } else {
                    Log.e("AssignParcelActivity", "Error fetching parcels: ${response.code}")
                }
            }
        })
    }

    private fun parseParcels(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val parcelsArray = jsonObject.getJSONArray("parcels")

            for (i in 0 until parcelsArray.length()) {
                val parcelObject = parcelsArray.getJSONObject(i)
                val parcelId = parcelObject.getString("parcelId")
                parcelsList.add(parcelId)
                parcelIdsMap[parcelId] = parcelId
            }
        } catch (e: Exception) {
            Log.e("AssignParcelActivity", "Error parsing parcels: ${e.message}")
        }
    }

    private fun assignParcelToRider() {
        val selectedRiderName = riderSpinner.selectedItem.toString()
        val selectedParcelId = parcelSpinner.selectedItem.toString()

        val riderId = riderIdsMap[selectedRiderName] ?: ""
        val parcelId = parcelIdsMap[selectedParcelId] ?: ""

        if (riderId.isEmpty() || parcelId.isEmpty()) {
            Toast.makeText(this, "Please select a rider and a parcel", Toast.LENGTH_SHORT).show()
            return
        }

        val jsonObject = JSONObject()
        jsonObject.put("riderId", riderId)
        jsonObject.put("parcelId", parcelId)

        val requestBody = RequestBody.create("application/json".toMediaType(), jsonObject.toString())

        val request = Request.Builder()
            .url("https://sdb-backend.onrender.com/api/v1/assign-courier")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("AssignParcelActivity", "Failed to assign parcel: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AssignParcelActivity, "Failed to assign parcel", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@AssignParcelActivity, "Parcel assigned successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("AssignParcelActivity", "Error assigning parcel: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@AssignParcelActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
