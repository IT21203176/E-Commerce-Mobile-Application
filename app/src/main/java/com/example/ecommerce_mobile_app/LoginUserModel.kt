package com.example.ecommerce_mobile_app

data class LoginUserModel(
    val id: String,
    val first_Name: String,
    val last_Name: String,
    val email: String,
    val nic: String,
    val address: String,
    val role: String,
    val isActive: Int
)
