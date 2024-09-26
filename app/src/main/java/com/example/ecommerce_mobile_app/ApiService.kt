package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.BrandModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/ProductLists")
    fun getProductList():Call<List<BrandModel>>
}