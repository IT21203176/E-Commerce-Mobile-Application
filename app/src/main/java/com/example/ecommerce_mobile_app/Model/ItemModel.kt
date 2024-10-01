package com.example.ecommerce_mobile_app.Model

import java.io.Serializable

data class ItemModel(
    /*val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String*/

    /*val title: String,
        val description: String,
        val picUrl: ArrayList<String> = ArrayList(),
        val size: ArrayList<String> = ArrayList(),
        val price: Double = 0.0,
        val rating: Double = 0.0,
        val numberInCart: Int
     */

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



