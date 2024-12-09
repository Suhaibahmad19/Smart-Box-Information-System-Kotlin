package com.example.smartbox19nov

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
data class ParcelPackage(
    val parcelID: String,
    val deliveryBoxID:String,
    val courierID:String,
    val size:String,
    val userId:String
):Parcelable{
    // Constructor for creating the object from a Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Writing object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(parcelID)
        parcel.writeString(deliveryBoxID)
        parcel.writeString(courierID)
        parcel.writeString(size)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelPackage> {
        override fun createFromParcel(parcel: Parcel): ParcelPackage {
            return ParcelPackage(parcel)
        }

        override fun newArray(size: Int): Array<ParcelPackage?> {
            return arrayOfNulls(size)
        }
    }
}
data class User(
    var email: String,
    var name:String,
    var role:String
): Parcelable {
    // Constructor for creating the object from a Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Writing object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(role)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}


class ManageUsersActivity : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var searchView: SearchView
    private val client = OkHttpClient()
    private val users = mutableListOf<User>()
    private val filteredUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        searchView = findViewById(R.id.searchView)
        userAdapter = UserAdapter(filteredUsers)

        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = userAdapter

        // Fetch users from backend
        fetchUsers()

        // Set up SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterUsers(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText)
                return false
            }
        })
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
                            // Initialize the filtered list with all users
                            filteredUsers.clear()
                            filteredUsers.addAll(users)
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

    private fun filterUsers(query: String?) {
        filteredUsers.clear()
        if (query.isNullOrEmpty()) {
            filteredUsers.addAll(users) // Show all users if query is empty
        } else {
            filteredUsers.addAll(users.filter { user ->
                user.name.contains(query, ignoreCase = true) ||
                        user.email.contains(query, ignoreCase = true) ||
                        user.role.contains(query, ignoreCase = true)
            })
        }
        userAdapter.notifyDataSetChanged()
    }
}
