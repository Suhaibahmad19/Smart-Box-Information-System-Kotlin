package com.example.smartbox19nov

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import org.json.JSONObject

class ViewLogsActivity : AppCompatActivity() {

    private lateinit var otpLogsTextView: TextView
    private lateinit var parcelLogsTextView: TextView

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_logs)

        // Initialize UI components
        otpLogsTextView = findViewById(R.id.otpLogsTextView)
        parcelLogsTextView = findViewById(R.id.parcelLogsTextView)

        // Fetch data from backend
        fetchOtpLogs()
        fetchParcelLogs()
    }

    private fun fetchOtpLogs() {
        val url = "https://sdb-backend.onrender.com/api/v1/otp-logs" // Replace with the actual backend endpoint for OTP logs

        Log.d("ViewLogsActivity", "Fetching OTP logs from URL: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewLogsActivity", "Failed to fetch OTP logs: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ViewLogsActivity, "Failed to load OTP logs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("ViewLogsActivity", "OTP logs API response received. Status: ${response.code}")
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("ViewLogsActivity", "OTP logs response body: $responseBody")
                    responseBody?.let {
                        val otpLogs = parseOtpLogs(it)
                        runOnUiThread {
                            otpLogsTextView.text = otpLogs
                        }
                    }
                } else {
                    Log.e("ViewLogsActivity", "OTP logs API error: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun fetchParcelLogs() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-parcels" // Replace with the actual backend endpoint for parcel logs

        Log.d("ViewLogsActivity", "Fetching Parcel logs from URL: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewLogsActivity", "Failed to fetch Parcel logs: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ViewLogsActivity, "Failed to load parcel logs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("ViewLogsActivity", "Parcel logs API response received. Status: ${response.code}")
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("ViewLogsActivity", "Parcel logs response body: $responseBody")
                    responseBody?.let {
                        val parcelLogs = parseParcelLogs(it)
                        runOnUiThread {
                            parcelLogsTextView.text = parcelLogs
                        }
                    }
                } else {
                    Log.e("ViewLogsActivity", "Parcel logs API error: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun parseOtpLogs(jsonResponse: String): String {
        return try {
            val jsonArray = JSONArray(jsonResponse)
            val otpLogs = StringBuilder()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)

                // Extract required fields
                val otpId = jsonObject.optString("otpId", "Unknown OTP ID")
                val phoneNumber = jsonObject.optString("phoneNumber", "03123456789")
                val status = jsonObject.optString("status", "Unknown Status")
                val error = if (jsonObject.isNull("error")) "NO" else "YES"
                val timestamp = jsonObject.optLong("timestamp", 0L)

                // Format the timestamp
                val formattedDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date(timestamp))

                otpLogs.append(
                    /*"OTP ID: $otpId\n*/"Phone: $phoneNumber\nStatus: $status\nError: $error\nTime: $formattedDate\n\n"
                )
            }

            if (otpLogs.isEmpty()) "No OTP logs available" else otpLogs.toString()
        } catch (e: Exception) {
            Log.e("ViewLogsActivity", "Error parsing OTP logs: ${e.message}")
            "Error parsing OTP logs"
        }
    }

    private fun parseParcelLogs(jsonResponse: String): String {
        return try {
            val jsonObject = JSONObject(jsonResponse) // Parse the response as a JSONObject
            val status = jsonObject.optString("status", "ERROR") // Check the status field
            if (status != "SUCCESS") {
                return "Error: Status not successful"
            }

            val parcelsArray = jsonObject.optJSONArray("parcels") ?: return "No parcels available" // Get the parcels array
            if (parcelsArray.length() == 0) {
                return "No parcels available"
            }

            val parcelLogs = StringBuilder()

            for (i in 0 until parcelsArray.length()) {
                val parcelObject = parcelsArray.getJSONObject(i)

                // Extract fields with null safety
                val parcelId = parcelObject.optString("parcelId", "Unknown Parcel ID")
                val size = parcelObject.optString("size", "Unknown Size")
                val destination = parcelObject.optString("destination", "Unknown Destination")
                val isFragile = parcelObject.optBoolean("isFragile", false)
                val createdAt = parcelObject.optLong("createdAt", 0L)
                val status = parcelObject.optString("status", "Unknown Status")

                // Format the createdAt timestamp
                val createdAtDate = if (createdAt > 0) {
                    java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                        .format(java.util.Date(createdAt))
                } else {
                    "Unknown Date"
                }

                // Append parsed data to the logs
                parcelLogs.append(
                    "Parcel-ID: $parcelId\n" +
                            "Size: $size\n" +
                            "Destination: $destination\n" +
                            "Fragile: ${if (isFragile) "Yes" else "No"}\n" +
                            "Created At: $createdAtDate\n" +
                            "Status: $status\n\n"
                )
            }

            if (parcelLogs.isEmpty()) "No parcel logs available" else parcelLogs.toString()
        } catch (e: Exception) {
            Log.e("ViewLogsActivity", "Error parsing Parcel logs: ${e.message}")
            "Error parsing Parcel logs: ${e.message}"
        }
    }


}
