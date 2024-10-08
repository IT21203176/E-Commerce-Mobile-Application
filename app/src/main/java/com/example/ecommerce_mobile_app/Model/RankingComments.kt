package com.example.ecommerce_mobile_app.Model

data class RankingComments(
    val id: String,
    val customerId: String,
    val vendorId: String,
    val ranking: Int,
    val customerName: String
)
