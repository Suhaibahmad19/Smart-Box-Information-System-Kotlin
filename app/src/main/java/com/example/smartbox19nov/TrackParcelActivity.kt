package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class TrackParcelActivity : AppCompatActivity() {

    private lateinit var parcelsRecyclerView: RecyclerView
    private val parcels = mutableListOf<Parcel>()
    private val client = OkHttpClient()
    private val loggedInUserEmail = "anotheremail@email.com" // Replace with the logged-in user's email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trackparcel)

        parcelsRecyclerView = findViewById(R.id.parcelsRecyclerView)
        parcelsRecyclerView.layoutManager = LinearLayoutManager(this)
        parcelsRecyclerView.adapter = ParcelAdapter(this,parcels)

        fetchParcels()
    }

    private fun fetchParcels() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-parcels" // Replace with your backend endpoint
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TrackParcelActivity", "Failed to fetch parcels: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@TrackParcelActivity, "Failed to load parcels", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        parseParcels(it)
                        runOnUiThread {
                            parcelsRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.e("TrackParcelActivity", "Error fetching parcels: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@TrackParcelActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun parseParcels(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val status = jsonObject.getString("status")
            if (status != "SUCCESS") return

            val parcelsArray = jsonObject.getJSONArray("parcels")
            parcels.clear()

            for (i in 0 until parcelsArray.length()) {
                val parcelObject = parcelsArray.getJSONObject(i)
                val userId = parcelObject.getString("userId")

                // Filter parcels based on the logged-in user's email
                if (userId == loggedInUserEmail) {
                    val parcelId = parcelObject.getString("parcelId")
                    val size = parcelObject.getString("size")
                    val destination = parcelObject.getString("destination")
                    val isFragile = parcelObject.getBoolean("isFragile")
                    val createdAt = parcelObject.getLong("createdAt")
                    val status = parcelObject.getString("status")

                    parcels.add(
                        Parcel(
                            parcelId,
                            size,
                            destination,
                            isFragile,
                            createdAt,
                            status
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("TrackParcelActivity", "Error parsing parcels: ${e.message}")
        }
    }
}
