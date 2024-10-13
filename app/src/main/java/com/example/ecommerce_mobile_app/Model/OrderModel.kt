package com.example.ecommerce_mobile_app.Model

data class OrderModel(
    val id: String? = null,
    val customerId: String,
    val orderCode: String? = null,
    val date: String,
    val totalPrice: Double,
    val status: Int = 0,
    val orderItems: List<OrderItemModel>,
    val orderItemCount: Int,
    val isCancellationRequested: Boolean = false,
    val cancellationNote: String? = null,
    val isCancellationApproved: Int = 0,
    val recipient_Name: String,
    val recipient_Email: String,
    val recipient_Contact: String,
    val recipient_Address: String,
    val customerFirstName: String,
    val customerLastName: String
)

data class OrderItemModel(
    val productId: String,
    val productName: String,
    val vendorId: String,
    val unitPrice: Int,
    val quantity: Int,
    val total: Double,
    val isDelivered: Boolean = false
)
