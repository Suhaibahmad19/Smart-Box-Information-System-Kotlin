package com.example.smartbox19nov

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

data class User(val username: String)

class ManageUsersActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private val filteredUsers = mutableListOf<User>() // For dynamic filtering

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        val usersRecyclerView: RecyclerView = findViewById(R.id.usersRecyclerView)
        val searchView: SearchView = findViewById(R.id.searchView)

        // Set up RecyclerView
        adapter = UserAdapter(filteredUsers)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = adapter

        // Fetch users from backend
        fetchUsersFromBackend()

        // Implement search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText)
                return true
            }
        })
    }

    private fun fetchUsersFromBackend() {
        // Replace with your backend URL
        val url = "http://10.0.2.2:8080/api/v1/get-users"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle network failure
                runOnUiThread {
                    Toast.makeText(this@ManageUsersActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val json = responseBody.string()
                        val jsonArray = JSONArray(json)

                        val users = mutableListOf<User>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username = jsonObject.getString("username")
                            users.add(User(username))
                        }

                        // Update UI on the main thread
                        runOnUiThread {
                            filteredUsers.clear()
                            filteredUsers.addAll(users)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    // Handle server error
                    runOnUiThread {
                        Toast.makeText(this@ManageUsersActivity, "Error: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

//    private fun fetchUsersFromBackend() {
//        val mockUsers = listOf(
//            User(username = "Usman"),
//            User(username = "Ali"),
//            User(username = "Fatima")
//        )
//
//        filteredUsers.clear()
//        filteredUsers.addAll(mockUsers)
//        adapter.notifyDataSetChanged()
//    }


    private fun filterUsers(query: String?) {
        val allUsers = filteredUsers.toList() // Work on a copy of the current list
        filteredUsers.clear()
        if (TextUtils.isEmpty(query)) {
            filteredUsers.addAll(allUsers)
        } else {
            val lowerCaseQuery = query!!.lowercase()
            val filteredList = allUsers.filter { user ->
                user.username.lowercase().contains(lowerCaseQuery)
            }
            filteredUsers.addAll(filteredList)
        }
        adapter.notifyDataSetChanged()
    }
}
