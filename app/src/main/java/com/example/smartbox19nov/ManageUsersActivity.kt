package com.example.smartbox19nov

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var adapter: UserAdapter
    private val users = listOf(
        User(username = "Usman"),
        User(username = "user1"),
        User(username = "user2"),
        User(username = "Ali"),
        User(username = "Zeeshan"),
        User(username = "Bilal"),
        User(username = "Hamza"),
        User(username = "Ahmed"),
        User(username = "Sarah"),
        User(username = "Fatima"),
        User(username = "Hassan"),
        User(username = "Amna")
    )

    private val filteredUsers = users.toMutableList() // For dynamic filtering

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        val usersRecyclerView: RecyclerView = findViewById(R.id.usersRecyclerView)
        val searchView: SearchView = findViewById(R.id.searchView)

        // Set up RecyclerView with the initial list
        adapter = UserAdapter(filteredUsers)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.adapter = adapter

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

    private fun filterUsers(query: String?) {
        filteredUsers.clear()
        if (TextUtils.isEmpty(query)) {
            filteredUsers.addAll(users)
        } else {
            val lowerCaseQuery = query!!.lowercase()
            val filteredList = users.filter { user ->
                user.username.lowercase().contains(lowerCaseQuery)
            }
            filteredUsers.addAll(filteredList)
        }
        adapter.notifyDataSetChanged() // Refresh the RecyclerView
    }
}
