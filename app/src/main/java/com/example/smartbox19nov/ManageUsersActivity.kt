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

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val client = OkHttpClient()
    private val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        userAdapter = UserAdapter(users)

        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = userAdapter

        // Fetch users from backend
        fetchUsers()
    }

    private fun fetchUsers() {
        val url = "https://sdb-backend.onrender.com/api/v1/get-users" // Replace with actual backend endpoint for users

        Log.d("ManageUsersActivity", "Fetching users from URL: $url")

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ManageUsersActivity", "Failed to fetch users: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ManageUsersActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("ManageUsersActivity", "Users API response received. Status: ${response.code}")
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("ManageUsersActivity", "Users response body: $responseBody")
                    responseBody?.let {
                        parseUsers(it)
                        runOnUiThread {
                            userAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.e("ManageUsersActivity", "Users API error: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@ManageUsersActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun parseUsers(jsonResponse: String) {
        try {
            // Parse the response as a JSONObject
            val jsonObject = JSONObject(jsonResponse)

            // Check if the status is "SUCCESS"
            val status = jsonObject.optString("status", "ERROR")
            if (status != "SUCCESS") {
                Log.e("ManageUsersActivity", "Error: ${jsonObject.optString("message", "Unknown error")}")
                return
            }

            // Extract the users array
            val usersArray = jsonObject.optJSONArray("users") ?: JSONArray()

            // Clear the existing list of users
            users.clear()

            // Loop through the users array and extract user details
            for (i in 0 until usersArray.length()) {
                val userObject = usersArray.getJSONObject(i)
                val name = userObject.optString("name", "Unknown Name")
                val email = userObject.optString("email", "Unknown Email")
                val role = userObject.optString("role", "Unknown Role")

                users.add(User(email, name, role))
            }

        } catch (e: Exception) {
            Log.e("ManageUsersActivity", "Error parsing users: ${e.message}")
        }
    }

}
