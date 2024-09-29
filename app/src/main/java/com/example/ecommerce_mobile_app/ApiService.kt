package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.BrandModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    //@GET("customer")
    //@GET("albums")
    //fun getProductList(): Call<List<BrandModel>>
    //@GET("ProductLists")
    //fun getProductList(): List<ProductListsItem>


    @GET("/Houses")
    suspend fun getProductList():List<ProductListsItem>
}