package com.example.smartbox19nov

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class RiderPannelActivity : AppCompatActivity() {

    private lateinit var areaSpinner: Spinner
    private val areasList = mutableListOf<String>()
    private lateinit var showParcelsButton: Button
    private lateinit var parcelRecyclerView: RecyclerView

    lateinit var riderEmail:String
    private val client = OkHttpClient()
    private val parcelsList = mutableListOf<Parcel>()
    private val adapter = ParcelAdapter(this,parcelsList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riderpannel)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.RiderParcelRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Assuming parcelsList is populated

        recyclerView.adapter = adapter
        riderEmail = intent.getStringExtra("email").toString()
        if (riderEmail != null) {
            Toast.makeText(this, "Email received: $riderEmail", Toast.LENGTH_SHORT).show()
            // Use the email as needed
        } else {
            Toast.makeText(this, "No email received", Toast.LENGTH_SHORT).show()
        }
        // Initialize views
        areaSpinner = findViewById(R.id.areaSpinner)
        //parcelRecyclerView = findViewById(R.id.parcelRecyclerView)


        // Handle "Show Parcels" button click
        //val selectedRider = riderSpinner.selectedItem.toString()
        //val riderId = riderIdsMap[selectedRider] ?: ""

        if (riderEmail != null) {
            fetchParcels()
            setupShowParcelsButton()
        } else {
            Toast.makeText(this, "Please select a valid rider", Toast.LENGTH_SHORT).show()
        }
    }



    private fun fetchParcels() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-parcels" // Endpoint for all parcels

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RiderPannelActivity", "Failed to fetch parcels: ${e.message}")
                runOnUiThread {
                    Toast.makeText(
                        this@RiderPannelActivity,
                        "Failed to load parcels",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseAndFilterParcels(it)
                        runOnUiThread {
                            setupAreaSpinner()
                        }
                    }
                } else {
                    Log.e("RiderPannelActivity", "Error fetching parcels: ${response.code}")
                }
            }
        })
    }


    private fun parseAndFilterParcels(jsonResponse: String) {
        parcelsList.clear()
        areasList.clear()

        try {
            val jsonObject = JSONObject(jsonResponse)
            val parcelsArray = jsonObject.getJSONArray("parcels")

            for (i in 0 until parcelsArray.length()) {
                val parcelObject = parcelsArray.getJSONObject(i)

                // Extract parcel data
                val parcelId = parcelObject.getString("parcelId")
                val size = "Medium"
                val destination = parcelObject.getString("destination")
                val isFragile = true //parcelObject.getBoolean("isFragile")
                val createdAt = parcelObject.getLong("createdAt")
                val status = "N/A"//parcelObject.getString("status")
                val userId = parcelObject.getString("userId")
                val deliveryBoxId = parcelObject.getString("deliveryBoxId")
                val courierId = parcelObject.getString("courierId") // Email of assigned rider

                // Only include parcels assigned to this courier
                if (courierId == riderEmail) {
                    val currentParcel = Parcel(
                        parcelId,
                        size,
                        destination,
                        isFragile,
                        createdAt,
                        status,
                        userId,
                        deliveryBoxId,
                        courierId
                    )
                    parcelsList.add(currentParcel)

                    // Add the destination to areas list (avoid duplicates)
                    if (!areasList.contains(destination)) {
                        areasList.add(destination)
                    }
                    print("Destination:$destination")
                }
            }
        } catch (e: Exception) {
            Log.e("RiderPannelActivity", "Error parsing parcels: ${e.message}")
        }
    }
    private fun filterParcelsByArea(selectedArea: String) {
        val filteredParcels = parcelsList.filter { it.destination == selectedArea }

        runOnUiThread {
            adapter.updateData(filteredParcels) // Use adapter's method to update data
        }
    }
    private fun setupShowParcelsButton() {
        val showParcelsButton: Button = findViewById(R.id.ShowassignParcelButton)
        showParcelsButton.setOnClickListener {
            val selectedArea = areaSpinner.selectedItem.toString()
            filterParcelsByArea(selectedArea)
        }
    }



    private fun setupAreaSpinner() {
        val adapter = ArrayAdapter(
            this@RiderPannelActivity,
            android.R.layout.simple_spinner_item,
            areasList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = adapter

        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedArea = areasList[position]
                filterParcelsByArea(selectedArea)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle case when no area is selected
            }
        }
    }
}

