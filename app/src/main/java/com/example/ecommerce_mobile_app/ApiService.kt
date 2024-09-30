package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.BrandModel
import com.example.ecommerce_mobile_app.Model.ItemModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    //@GET("customer")
    //@GET("albums")
    //fun getProductList(): Call<List<BrandModel>>
    @GET("ProductLists")
    suspend fun getProductList(): List<ProductListsItem>


    //@GET("/Houses")
    //suspend fun getProductList():List<ProductListsItem>

    //@GET("/photos")
    //suspend fun getPopProductList():List<ItemModel>

    @GET("users")
    suspend fun getPopProductList():List<ItemModel>
}