package com.example.ecommerce_mobile_app.Model

data class UserModel(
    val id: String,
    val first_Name: String,
    val last_Name: String,
    val email: String,
    val passwordHash: String,
    val nic: String,
    val address: String,
    val role: String,
    val averageRating: Double? = 0.0,
    val isActive: Int
)
