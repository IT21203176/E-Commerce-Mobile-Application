package com.example.ecommerce_mobile_app.Model


/*data class Order(
    val id: String,
    val customerId: String,
    val orderCode: String,
    val date: String,
    val totalPrice: Double,
    val status: Int,
    val orderItems: List<OrderItem>,
    val orderItemCount: Int,
    val isCancellationRequested: Boolean,
    val cancellationNote: String,
    val isCancellationApproved: Int,
    @SerializedName("Recipient_Name") val recipientName: String,
    @SerializedName("Recipient_Email") val recipientEmail: String,
    @SerializedName("Recipient_Contact") val recipientContact: String,
    @SerializedName("Recipient_Address") val recipientAddress: String,
    val customerFirstName: String,
    val customerLastName: String
)

data class OrderItem(
    val productId: String,
    val productName: String,
    val vendorId: String,
    val unitPrice: Int,
    val quantity: Int,
    val total: Double,
    val isDelivered: Boolean
)*/

data class OrderItem(
    val productId: String,
    val productName: String?,
    val vendorId: String,
    val unitPrice: Double,
    val quantity: Int,
    val total: Double,
    val isDelivered: Boolean
)

data class Order(
    val id: String? = null,
    val customerId: String,
    val date: String,
    val orderCode: String? = null,
    val totalPrice: Double,
    val status: Int,
    val orderItems: List<OrderItem>,
    val orderItemCount: Int,
    var isCancellationRequested: Boolean,
    val cancellationNote: String?,
    val isCancellationApproved: Int,
    val recipient_Name: String,
    val recipient_Email: String,
    val recipient_Contact: String,
    val recipient_Address: String,
    val customerFirstName: String,
    val customerLastName: String
)


