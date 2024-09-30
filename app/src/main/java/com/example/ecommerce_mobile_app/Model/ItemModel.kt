package com.example.ecommerce_mobile_app.Model

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

    val id: Int,
    val name: String,
    val company: String,
    val username: String,
    val email: String,
    val address: String,
    val zip: String,
    val state: String,
    val country: String,
    val phone: String,
    val photo: String
)



