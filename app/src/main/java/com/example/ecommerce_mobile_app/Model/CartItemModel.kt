package com.example.ecommerce_mobile_app.Model

import java.io.Serializable

data class CartItemModel(
    val id: String,
    val name: String,
    val price: Int,
    val quantity: Int
) : Serializable