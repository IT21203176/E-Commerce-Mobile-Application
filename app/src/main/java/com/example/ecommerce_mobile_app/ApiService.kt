package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.Comments
import com.example.ecommerce_mobile_app.Model.CustomerModel
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.Model.NotificationModel
import com.example.ecommerce_mobile_app.Model.Order
import com.example.ecommerce_mobile_app.Model.OrderModel
import com.example.ecommerce_mobile_app.Model.RankingComments
import com.example.ecommerce_mobile_app.Model.UserLoginModel
import com.example.ecommerce_mobile_app.Model.UserModel
import com.example.ecommerce_mobile_app.Model.UserProfileModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
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

    @PUT("Users/update/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Header("Authorization") token: String,
        @Body updatedUserData: UserModel
    ): Response<UserModel>

    @POST("Orders")
    suspend fun createOrder(@Body order: Order): Response<Order>

    @POST("RankingComments")
    suspend fun postRanking(@Body rankingComments: RankingComments): Response<RankingComments>

    @POST("RankingComments/addComment")
    fun addComment(@Body comment: Comments): Call<Void>

    @GET("Orders")
    fun getOrdersByCustomerId(
        @Query("customerId") customerId: String
    ): Call<List<Order>>

    @GET("Orders/{id}")
    fun getOrderDetails(@Path("id") orderId: String): Call<Order>

    @PATCH("Orders/{id}/cancel-request")
    suspend fun requestOrderCancel(
        @Path("id") orderId: String,
        @Body cancellationNote: String
    ): Response<Order>

    @GET("Notifications/{receiverId}")
    fun getNotifications(@Path("receiverId") receiverId: String): Call<List<NotificationModel>>

    @GET("RankingComments/vendorComments/{vendorId}")
    fun getComments(@Path("vendorId") vendorId: String): Call<List<Comments>>
}

data class LoginResponseModel(
    val token: String,
    val user: LoginUserModel,
    val message: String
)