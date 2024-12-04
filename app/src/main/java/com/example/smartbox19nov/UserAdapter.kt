package com.example.smartbox19nov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emailTextView: TextView = view.findViewById(R.id.emailTextView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val roleTextView: TextView = view.findViewById(R.id.roleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.emailTextView.text = "Email: ${user.email}"
        holder.nameTextView.text = "Name: ${user.name}"
        holder.roleTextView.text = "Role: ${user.role}"
    }

    override fun getItemCount(): Int = users.size
}
