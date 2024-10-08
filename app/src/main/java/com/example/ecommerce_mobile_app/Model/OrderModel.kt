package com.example.ecommerce_mobile_app.Model

data class OrderModel(
    val id: String?,
    val customerId: String,
    val date: String,
    val totalPrice: Double,
    val status: Int,
    val orderItems: List<OrderItem>,
    val orderItemCount: Int,
    val isCancellationRequested: Boolean,
    val cancellationNote: String?,
    val isCancellationApproved: Int,
    val recipient_Name: String,
    val recipient_Email: String,
    val recipient_Contact: String,
    val recipient_Address: String,
    val customerFirstName: String,
    val customerLastName: String
)

data class OrderItem(
    val productId: String,
    val productName: String,
    val vendorId: String,
    val unitPrice: Double,
    val quantity: Int,
    val total: Double,
    val isDelivered: Boolean
)
