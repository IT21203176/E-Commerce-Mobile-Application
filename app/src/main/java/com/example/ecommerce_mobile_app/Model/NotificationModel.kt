package com.example.ecommerce_mobile_app.Model

data class NotificationModel(
    val id: String,
    val receiverId: String,
    val message: String,
    val isVisibleToAdmin: Boolean,
    val isVisibleToCSR: Boolean,
    val isVisibleToVendor: Boolean,
    val createdAt: String
)
