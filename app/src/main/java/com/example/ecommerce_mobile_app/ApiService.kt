package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.CustomerModel
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.Model.UserLoginModel
import com.example.ecommerce_mobile_app.Model.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("ProductLists")
    suspend fun getProductList(): List<ProductListsItem>

    @GET("Products")
    suspend fun getPopProductList():List<ItemModel>

    @GET("Products")
    suspend fun getProductsByList(@Query("productListName") productListName: String): List<ItemModel>

    @POST("Users/register")
    fun registerUser(@Body userModel: UserModel): Call<UserModel>

    @POST("Users/customer-login")
    suspend fun loginUser(@Body userLogin: UserLoginModel): LoginResponseModel

    @GET("Users/{id}")
    suspend fun getUserDetails(@Path("id") userId: String, @Header("Authorization") token: String): UserModel

    /*@PUT("Users/update/{id}")
    suspend fun updateUserDetails(@Path("id") userId: String, @Header("Authorization") token: String, @Body user: UserModel): Response<ResponseBody>*/

}

data class LoginResponseModel(
    val token: String,
    val user: LoginUserModel,
    val message: String
)

