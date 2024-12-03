/*
package com.example.smartbox19nov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ViewLogsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_logs)

        // References to the TextViews
        val otpLogsTextView: TextView = findViewById(R.id.otpLogsTextView)
        val parcelLogsTextView: TextView = findViewById(R.id.parcelLogsTextView)

        // Example data for OTP logs
        val otpLogs = """
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
            OTP Log 1: 123456
            OTP Log 2: 654321
            OTP Log 3: 111222
        """.trimIndent()

        // Example data for parcel logs
        val parcelLogs = """
            Parcel 1: Small, Destination A
            Parcel 2: Medium, Destination B
            Parcel 3: Large, Destination C
        """.trimIndent()

        // Set the logs
        otpLogsTextView.text = otpLogs
        parcelLogsTextView.text = parcelLogs
    }
}
*/

//package com.example.smartbox19nov
//
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import okhttp3.*
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.IOException
//
//class ViewLogsActivity : AppCompatActivity() {
//
//    private lateinit var otpLogsTextView: TextView
//    private lateinit var parcelLogsTextView: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_view_logs)
//
//        // Initialize UI components
//        otpLogsTextView = findViewById(R.id.otpLogsTextView)
//        parcelLogsTextView = findViewById(R.id.parcelLogsTextView)
//
//        // Fetch data from backend
//        fetchOtpLogs()
//        fetchParcelLogs()
//    }
//
//    private fun fetchOtpLogs() {
//        val url = "http://10.0.2.2:8080/api/v1/get-otp-logs" // Replace with the actual backend endpoint for OTP logs
//        val client = OkHttpClient()
//
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@ViewLogsActivity, "Failed to load OTP logs", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    responseBody?.let {
//                        val otpLogs = parseOtpLogs(it)
//                        runOnUiThread {
//                            otpLogsTextView.text = otpLogs
//                        }
//                    }
//                } else {
//                    runOnUiThread {
//                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//    }
//
//    private fun fetchParcelLogs() {
//        val url = "http://10.0.2.2:8080/api/v1/get-parcels" // Replace with the actual backend endpoint for parcel logs
//        val client = OkHttpClient()
//
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@ViewLogsActivity, "Failed to load parcel logs", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    responseBody?.let {
//                        val parcelLogs = parseParcelLogs(it)
//                        runOnUiThread {
//                            parcelLogsTextView.text = parcelLogs
//                        }
//                    }
//                } else {
//                    runOnUiThread {
//                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//    }
//
//    private fun parseOtpLogs(jsonResponse: String): String {
//        val jsonArray = JSONArray(jsonResponse)
//        val otpLogs = StringBuilder()
//
//        for (i in 0 until jsonArray.length()) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            val otp = jsonObject.getString("otp")
//            val phoneNumber = jsonObject.getString("phoneNumber")
//            otpLogs.append("OTP: $otp, Phone: $phoneNumber\n")
//        }
//
//        return if (otpLogs.isEmpty()) "No OTP logs available" else otpLogs.toString()
//    }
//
//    private fun parseParcelLogs(jsonResponse: String): String {
//        val jsonArray = JSONArray(jsonResponse)
//        val parcelLogs = StringBuilder()
//
//        for (i in 0 until jsonArray.length()) {
//            val jsonObject = jsonArray.getJSONObject(i)
//            val parcelId = jsonObject.getString("parcelId")
//            val size = jsonObject.getString("size")
//            val destination = jsonObject.getString("destination")
//            parcelLogs.append("Parcel: $parcelId, Size: $size, Destination: $destination\n")
//        }
//
//        return if (parcelLogs.isEmpty()) "No parcel logs available" else parcelLogs.toString()
//    }
//}

package com.example.smartbox19nov

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class ViewLogsActivity : AppCompatActivity() {

    private lateinit var otpLogsTextView: TextView
    private lateinit var parcelLogsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_logs)

        otpLogsTextView = findViewById(R.id.otpLogsTextView)
        parcelLogsTextView = findViewById(R.id.parcelLogsTextView)

        fetchOtpLogs()
        fetchParcelLogs()
    }

    private fun fetchOtpLogs() {
        val url = "http://10.0.2.2:8080/api/v1/get-otp-logs" // Replace with correct endpoint
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ViewLogsActivity, "Failed to load OTP logs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val otpLogs = parseLogs(it)
                        runOnUiThread {
                            otpLogsTextView.text = otpLogs
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun fetchParcelLogs() {
        val url = "http://10.0.2.2:8080/api/v1/get-parcels" // Replace with correct endpoint
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ViewLogsActivity, "Failed to load parcel logs", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val parcelLogs = parseLogs(it)
                        runOnUiThread {
                            parcelLogsTextView.text = parcelLogs
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ViewLogsActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun parseLogs(jsonResponse: String): String {
        val jsonArray = JSONArray(jsonResponse)
        val logs = StringBuilder()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            logs.append(jsonObject.toString(4)) // Formats each log entry
            logs.append("\n\n")
        }

        return logs.toString()
    }
}
