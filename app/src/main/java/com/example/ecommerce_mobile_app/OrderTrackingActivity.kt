package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.OrderItemAdapter
import com.example.ecommerce_mobile_app.Model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var orderIdTextView: TextView
    private lateinit var orderDateTextView: TextView
    private lateinit var orderCustomerName: TextView
    private lateinit var recipientNameTextView: TextView
    private lateinit var recipientContactTextView: TextView
    private lateinit var recipientAddressTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cancellationNoteEditText: EditText
    private lateinit var cancelButton: Button

    private val apiService: ApiService = RetrofitClient.apiService
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order_tracking)

        // Initialize UI components
        orderIdTextView = findViewById(R.id.textView44)
        orderDateTextView = findViewById(R.id.ord_date)
        recipientNameTextView = findViewById(R.id.ord_recep_name)
        recipientContactTextView = findViewById(R.id.ord_recep_contact)
        recipientAddressTextView = findViewById(R.id.ord_recep_address)
        orderCustomerName = findViewById(R.id.ord_cust_name)
        recyclerView = findViewById(R.id.product_n_vendor_wise_status)
        cancellationNoteEditText = findViewById(R.id.cancellationRequestNote)
        cancelButton = findViewById(R.id.orderCancellationBtn)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve the order ID passed from the adapter
        val orderId = intent.getStringExtra("ORDER_ID")
        Log.d("OrderTrackingActivity", "Order ID: $orderId")

        // Fetch and display order details
        orderId?.let { fetchOrderDetails(it) }

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        cancelButton.setOnClickListener {
            val cancellationNote = cancellationNoteEditText.text.toString().trim()

            if (cancellationNote.isEmpty()) {
                Toast.makeText(this, "Please provide a cancellation reason.", Toast.LENGTH_SHORT).show()
            } else {
                val orderId = orderIdTextView.text.toString().trim() // Fetch the order id from orderIdTextView

                if (orderId.isEmpty()) {
                    Toast.makeText(this, "Order ID is not available. Cannot request cancellation.", Toast.LENGTH_SHORT).show()
                } else {
                    sendCancelRequest(orderId, cancellationNote)
                }
            }
        }
    }



    private fun fetchOrderDetails(orderId: String) {
        apiService.getOrderDetails(orderId).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    val order = response.body()
                    order?.let {
                        displayOrderDetails(it)
                    }
                } else {
                    Toast.makeText(this@OrderTrackingActivity, "Failed to fetch order details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@OrderTrackingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayOrderDetails(order: Order) {
        orderIdTextView.text = order.id
        orderDateTextView.text = formatDate(order.date)
        orderCustomerName.text = "${order.customerFirstName} ${order.customerLastName}"
        recipientNameTextView.text = order.recipient_Name
        recipientContactTextView.text = order.recipient_Contact
        recipientAddressTextView.text = order.recipient_Address

        // Set up RecyclerView for order items
        val orderItemAdapter = OrderItemAdapter(this, order.orderItems)
        recyclerView.adapter = orderItemAdapter
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date: Date? = inputFormat.parse(dateString)

            // Define the desired output format
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Standard format
            outputFormat.format(date ?: Date()) // Use the current date if parsing fails
        } catch (e: Exception) {
            e.printStackTrace()
            dateString // Return the original string if parsing fails
        }
    }

    private fun getOrderFromIntent(): Order {
        // Extract the Order object from the intent or retrieve it through a method.
        // Assuming the order is passed as a serializable object.
        return intent.getSerializableExtra("ORDER") as Order
    }

    private fun sendCancelRequest(orderId: String, cancellationNote: String) {
        lifecycleScope.launch {
            try {
                // Prepare the PATCH request
                val response = withContext(Dispatchers.IO) {
                    apiService.requestOrderCancel(orderId, cancellationNote)
                }
                if (response.isSuccessful) {
                    // Update the order state locally
                    updateOrderStateAfterCancel(order)
                    Toast.makeText(this@OrderTrackingActivity, "Cancel request sent successfully!", Toast.LENGTH_SHORT).show()
                    cancelButton.text = "Cancel Request Pending"
                    cancelButton.isEnabled = false
                } else {
                    Toast.makeText(this@OrderTrackingActivity, "Cancel request sent successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@OrderTrackingActivity, "Cancel request sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateOrderStateAfterCancel(order: Order) {
        order.isCancellationRequested = true
        // You may also update any UI components or local database here if needed
    }

}