package com.example.smartbox19nov
//val otpId: String = "",
//val phoneNumber: String = "",
//val serviceProviderId: String = "",
//val otp: String = "",
//val createdAt: Long = System.currentTimeMillis(),
//val expiresAt: Long = System.currentTimeMillis() + 600000, // 10 minutes expiry
//val status: String = "PENDING"

//"parcelId": "5cc2a416-587d-4e6d-a19b-d8811a702152",
//"destination": "Okara",
//"createdAt": 1733770649632,
//"userId": "UsmanUser@email.com",
//"deliveryBoxId": "3c07a56e-2a7e-4dc6-8b88-05c665e61d95",
//"courierId": "c@email.com"

data class Parcel(
    val parcelId: String,
    val size: String,
    val destination: String,
    val isFragile: Boolean,
    val createdAt: Long,
    val status: String,
    val userId: String = "N/A",
    val deliveryBoxId:String = "N/A",
    val courierId:String = "N/A"
    )
