package com.example.ecommerce_mobile_app.Model

import java.io.Serializable

data class ItemModel(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val stock: Int,
    val lowStockLvl: Int,
    val stockStatus: String,
    val image: String,
    val isActive: Boolean,
    val productListName: String
): Serializable



