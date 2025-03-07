package com.example.ecommerce_mobile_app.Model

data class UserProfileModel(
    val id: String,
    val first_Name: String,
    val last_Name: String,
    val email: String,
    val nic: String,
    val address: String,
    val passwordHash: String? = null
)
