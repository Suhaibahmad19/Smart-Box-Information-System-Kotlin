package com.example.smartbox19nov

data class Parcel(
    val parcelId: String,
    val size: String,
    val destination: String,
    val isFragile: Boolean,
    val createdAt: Long,
    val status: String
)
