package com.example.smartbox19nov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParcelAdapter(private val parcels: List<Parcel>) : RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder>() {

    class ParcelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parcelIdTextView: TextView = view.findViewById(R.id.parcelIdTextView)
        val destinationTextView: TextView = view.findViewById(R.id.destinationTextView)
        val sizeTextView: TextView = view.findViewById(R.id.sizeTextView)
        val fragileTextView: TextView = view.findViewById(R.id.fragileTextView)
        val createdAtTextView: TextView = view.findViewById(R.id.createdAtTextView)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parcel, parent, false)
        return ParcelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParcelViewHolder, position: Int) {
        val parcel = parcels[position]
        holder.parcelIdTextView.text = "Parcel ID: ${parcel.parcelId}"
        holder.destinationTextView.text = "Destination: ${parcel.destination}"
        holder.sizeTextView.text = "Size: ${parcel.size}"
        holder.fragileTextView.text = "Fragile: ${parcel.isFragile}"
        holder.createdAtTextView.text = "Created At: ${parcel.createdAt}"
        holder.statusTextView.text = "Status: ${parcel.status}"
    }

    override fun getItemCount(): Int = parcels.size
}
