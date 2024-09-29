package com.example.ecommerce_mobile_app.Model

import com.google.gson.annotations.SerializedName

data class BrandModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("title")
    val title: String
)
