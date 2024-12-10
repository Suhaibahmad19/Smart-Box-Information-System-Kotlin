package com.example.smartbox19nov

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParcelAdapter(
    private val context: Context,
    private var parcels: List<Parcel>,
) : RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder>() {

    class ParcelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parcelIdTextView: TextView = view.findViewById(R.id.parcelIdTextView)
        val destinationTextView: TextView = view.findViewById(R.id.destinationTextView)
        val sizeTextView: TextView = view.findViewById(R.id.sizeTextView)
        val fragileTextView: TextView = view.findViewById(R.id.fragileTextView)
        val createdAtTextView: TextView = view.findViewById(R.id.createdAtTextView)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
        val deliverParcelButton: Button = view.findViewById(R.id.deliver_parcel_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParcelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parcel, parent, false)
        return ParcelViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ParcelViewHolder, position: Int) {
        val parcel = parcels[position]
        holder.parcelIdTextView.text = "Parcel ID: ${parcel.parcelId}"
        holder.destinationTextView.text = "Destination: ${parcel.destination}"
        holder.sizeTextView.text = "Size: ${parcel.size}"
        holder.fragileTextView.text = "Fragile: ${parcel.isFragile}"
        holder.createdAtTextView.text = "Created At: ${parcel.createdAt}"
        holder.statusTextView.text = "Status: ${parcel.status}"

        holder.deliverParcelButton.setOnClickListener {
            val intent = Intent(context, SmartBoxEmulator::class.java)
            intent.putExtra("parcelId", parcel.parcelId)
            intent.putExtra("parcelDestination",parcel.destination)
            intent.putExtra("CALL_FROM","ParcelDeliveryByRider")
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newParcels: List<Parcel>) {
        parcels = newParcels
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = parcels.size
}
