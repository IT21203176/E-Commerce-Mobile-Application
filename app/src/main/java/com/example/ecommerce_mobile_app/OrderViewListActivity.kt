package com.example.ecommerce_mobile_app

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.OrderAdapter
import com.example.ecommerce_mobile_app.Model.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewListActivity : AppCompatActivity() {

    private lateinit var orderAdapter: OrderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_view_list)

        // Initialize views
        recyclerView = findViewById(R.id.viewMyOrders)
        backButton = findViewById(R.id.imageView24)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch userId from SharedPreferences
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE) // Corrected key
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            fetchOrders(userId)
        } else {
            Toast.makeText(this, "User ID not found. Please log in.", Toast.LENGTH_SHORT).show()
        }

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close the activity and go back
        }
    }

    /*private fun fetchOrders(customerId: String) {
        val apiService = RetrofitClient.apiService

        apiService.getOrdersByCustomerId(customerId).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val orders = response.body() ?: emptyList()
                    setupRecyclerView(orders)
                } else {
                    Toast.makeText(this@OrderViewListActivity, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Log.e("OrderViewListActivity", "Error fetching orders", t)
                Toast.makeText(this@OrderViewListActivity, "Error fetching orders", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun fetchOrders(userId: String) {
        val apiService = RetrofitClient.apiService

        apiService.getOrdersByCustomerId(userId).enqueue(object : Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                if (response.isSuccessful) {
                    val allOrders = response.body() ?: emptyList()

                    // Filter orders where customerId matches logged-in user's userId
                    val userOrders = allOrders.filter { order -> order.customerId == userId }

                    if (userOrders.isNotEmpty()) {
                        setupRecyclerView(userOrders)
                    } else {
                        Toast.makeText(this@OrderViewListActivity, "No orders found for this user.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@OrderViewListActivity, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                Log.e("OrderViewListActivity", "Error fetching orders", t)
                Toast.makeText(this@OrderViewListActivity, "Error fetching orders", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(orderList: List<Order>) {
        orderAdapter = OrderAdapter(orderList) { order ->
            // Handle track button click, you can start an activity to track the order
            Toast.makeText(this, "Tracking order: ${order.orderCode}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = orderAdapter
    }
}

