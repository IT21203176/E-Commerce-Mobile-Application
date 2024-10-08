package com.example.ecommerce_mobile_app.Model

data class Comments(
    val id: String,
    val customerId: String,
    val vendorId: String,
    val comment: String,
    val customerName: String
)
