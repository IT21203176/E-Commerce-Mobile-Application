package com.example.ecommerce_mobile_app

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_mobile_app.Adapter.CheckoutCartAdapter
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.Model.Order
import com.example.ecommerce_mobile_app.Model.OrderItem
import com.example.ecommerce_mobile_app.Model.UserModel
import com.example.ecommerce_mobile_app.databinding.ActivityCheckoutBinding
import com.example.project1762.Helper.ChangeNumberItemsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var checkoutCartAdapter: CheckoutCartAdapter
    private lateinit var cartItems: ArrayList<ItemModel>

    companion object {
        const val DELIVERY_FEE = 300.0
        const val TAX_RATE = 0.05
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkoutBtn : Button = findViewById(R.id.checkout_btn)

        // Retrieve the data from the Intent
        val subTotal = intent.getDoubleExtra("SUB_TOTAL", 0.0)
        val deliveryFee = intent.getDoubleExtra("DELIVERY_FEE", 0.0)
        val tax = intent.getDoubleExtra("TAX", 0.0)
        val total = intent.getDoubleExtra("TOTAL", 0.0)

        // Retrieve the cart items passed via Intent
        cartItems = intent.getSerializableExtra("CART_ITEMS") as ArrayList<ItemModel>

        // Format the values to display
        val decimalFormat = DecimalFormat("#,###.00")
        binding.subTotAmountTxt.text = "LKR. ${decimalFormat.format(subTotal)}"
        binding.DeliveryAmountTxt.text = "LKR. ${decimalFormat.format(deliveryFee)}"
        binding.TaxAmountTxt.text = "LKR. ${decimalFormat.format(tax)}"
        binding.TotalAmountTxt.text = "LKR. ${decimalFormat.format(total)}"

        // Setup RecyclerView for cart items in CheckoutActivity
        setupRecyclerView()

        updateTotals()

        displayUserDetails()

        checkoutBtn.setOnClickListener {
            //showPaymentSuccessPopupBox()

            val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val customerId = sharedPref.getString("userId", "") ?: ""
            val firstName = sharedPref.getString("firstName", "") ?: ""
            val lastName = sharedPref.getString("lastName", "") ?: ""
            val email = sharedPref.getString("email", "") ?: ""

            // Collect recipient details from the UI
            val recipientName = binding.OrderRecepName.text.toString().trim()
            val recipientEmail = binding.OrderRecepEmail.text.toString().trim()
            val recipientContact = binding.OrderRecepMobile.text.toString().trim()
            val recipientAddress = binding.OrderRecepAddress.text.toString().trim()

            // Validate recipient details
            if (recipientName.isEmpty()) {
                Toast.makeText(this, "Recipient Name is required", Toast.LENGTH_SHORT).show()
            }
            if (recipientEmail.isEmpty()) {
                Toast.makeText(this, "Recipient Email is required", Toast.LENGTH_SHORT).show()
            }
            if (recipientContact.isEmpty()) {
                Toast.makeText(this, "Recipient Contact is required", Toast.LENGTH_SHORT).show()
            }
            if (recipientAddress.isEmpty()) {
                Toast.makeText(this, "Recipient Address is required", Toast.LENGTH_SHORT).show()
            }


            val orderItems = cartItems.map { item ->
                val quantity = checkoutCartAdapter.getQuantityForItem(item)
                OrderItem(
                    productId = item.id,
                    productName = item.name,
                    vendorId = item.product_idVendor,
                    unitPrice = item.price.toDouble(),
                    quantity = quantity,
                    total = item.price * quantity.toDouble(),
                    isDelivered = false
                )
            }

            val orderCode = "ORD" + System.currentTimeMillis().toString()

            val order = Order(
                customerId = customerId,
                date = "2024-10-07T15:04:29.414Z",
                orderCode = orderCode,
                totalPrice = total,
                status = 0,
                orderItems = orderItems,
                orderItemCount = orderItems.size,
                isCancellationRequested = false,
                cancellationNote = "",
                isCancellationApproved = 0,
                recipient_Name = recipientName,
                recipient_Email = recipientEmail,
                recipient_Contact = recipientContact,
                recipient_Address = recipientAddress,
                customerFirstName = firstName,
                customerLastName = lastName
            )

            createOrder(order)
        }

        binding.backBtn1.setOnClickListener {
            onBackPressed() // Navigate back to the previous activity
        }

    }

    private fun setupRecyclerView() {
        // Initialize the CartAdapter with the cart items and set it to RecyclerView
        checkoutCartAdapter = CheckoutCartAdapter(cartItems, this, object :
            ChangeNumberItemsListener {
            override fun onChanged() {
                // Recalculate totals whenever the cart items change
                updateTotals()
            }
        })
        binding.viewOrderCartItems.layoutManager = LinearLayoutManager(this)
        binding.viewOrderCartItems.adapter = checkoutCartAdapter
    }


    private fun showPaymentSuccessPopupBox() {
        val dialog = Dialog(this)
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.custom_payment_successful_box, null)

        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val backToShopBtn: Button = dialogView.findViewById(R.id.checkout_btn)

        backToShopBtn.setOnClickListener {
            dialog.dismiss()

            // Retrieve user details from SharedPreferences
            val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
            val firstName = sharedPref.getString("firstName", "")
            val lastName = sharedPref.getString("lastName", "")

            // Log the retrieved values for debugging
            Log.d("PaymentSuccess", "Retrieved firstName: $firstName")
            Log.d("PaymentSuccess", "Retrieved lastName: $lastName")

            // Create an intent to navigate back to ProductLActivity
            val intent = Intent(this, ProductLActivity::class.java)
            // Pass the user's first and last name with the intent
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            startActivity(intent)
        }

        dialog.show()
    }


    private fun updateTotals() {
        val decimalFormat = DecimalFormat("#,###.00")

        val subTotal = calculateSubtotal()
        val tax = subTotal * TAX_RATE
        val total = subTotal + DELIVERY_FEE + tax

        // Update the UI with the calculated values
        binding.subTotAmountTxt.text = "LKR. ${decimalFormat.format(subTotal)}"
        binding.DeliveryAmountTxt.text = "LKR. ${decimalFormat.format(DELIVERY_FEE)}"
        binding.TaxAmountTxt.text = "LKR. ${decimalFormat.format(tax)}"
        binding.TotalAmountTxt.text = "LKR. ${decimalFormat.format(total)}"
    }

    private fun calculateSubtotal(): Double {
        var subTotal = 0.0
        for (item in checkoutCartAdapter.listItemSelected) {
            val quantity = checkoutCartAdapter.getQuantityForItem(item) // Get quantity for the item
            subTotal += item.price * quantity
        }
        return subTotal
    }

    private fun displayUserDetails() {
        val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val firstName = sharedPref.getString("firstName", "")
        val lastName = sharedPref.getString("lastName", "")

        binding.customersFName.text = firstName
        binding.customersLName.text = lastName
    }

    /*private fun createOrder() {
        // Get user session details
        val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val customerId = sharedPref.getString("customerId", "") ?: ""
        val firstName = sharedPref.getString("firstName", "") ?: ""
        val lastName = sharedPref.getString("lastName", "") ?: ""

        // Collect recipient details from the UI
        val recipientName = binding.OrderRecepName.text.toString()
        val recipientEmail = binding.OrderRecepEmail.text.toString()
        val recipientContact = binding.OrderRecepMobile.text.toString()
        val recipientAddress = binding.OrderRecepAddress.text.toString()

        // Prepare the order items from the cart
        val orderItems = cartItems.map { item ->
            OrderItem(
                productId = item.id,
                productName = item.name,
                vendorId = item.productVendorName,
                unitPrice = item.price,
                quantity = checkoutCartAdapter.getQuantityForItem(item),
                total = item.price * checkoutCartAdapter.getQuantityForItem(item).toDouble(),
                isDelivered = false
            )
        }

        // Calculate total price
        val subTotal = calculateSubtotal()
        val tax = subTotal * TAX_RATE
        val totalPrice = subTotal + DELIVERY_FEE + tax

        // Create an order model
        val order = Order(
            id = "", // This will be generated by the server
            customerId = customerId,
            orderCode = "", // Auto-generated by the backend
            date = "2024-10-12T04:48:54.151Z", // Ideally, get the current timestamp programmatically
            totalPrice = totalPrice,
            status = 0, // Default status
            orderItems = orderItems,
            orderItemCount = orderItems.size,
            isCancellationRequested = false,
            cancellationNote = "",
            isCancellationApproved = 0,
            recipientName = recipientName,
            recipientEmail = recipientEmail,
            recipientContact = recipientContact,
            recipientAddress = recipientAddress,
            customerFirstName = firstName,
            customerLastName = lastName
        )

        val call = RetrofitClient.apiService.createOrder(order)

        call.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    showPaymentSuccessPopupBox() // Show success dialog
                } else {
                    // Handle error
                    Toast.makeText(this@CheckoutActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@CheckoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    /*private fun createOrder() {
        // Get user session details
        val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val customerId = sharedPref.getString("userId", "") ?: ""
        val firstName = sharedPref.getString("firstName", "") ?: ""
        val lastName = sharedPref.getString("lastName", "") ?: ""

        // Collect recipient details from the UI
        val recipientName = binding.OrderRecepName.text.toString()
        val recipientEmail = binding.OrderRecepEmail.text.toString()
        val recipientContact = binding.OrderRecepMobile.text.toString()
        val recipientAddress = binding.OrderRecepAddress.text.toString()

        // Validate recipient details
        if (recipientName.isEmpty() || recipientEmail.isEmpty() || recipientContact.isEmpty() || recipientAddress.isEmpty()) {
            Toast.makeText(this, "All recipient details are required!", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare order items from cart
        val orderItems = cartItems.map { item ->
            OrderItem(
                productId = item.id,
                productName = item.name,
                vendorId = item.productVendorName,
                unitPrice = item.price,
                quantity = checkoutCartAdapter.getQuantityForItem(item),
                total = item.price * checkoutCartAdapter.getQuantityForItem(item).toDouble(),
                isDelivered = false
            )
        }

        // Calculate order total price
        val subTotal = calculateSubtotal()
        val tax = subTotal * TAX_RATE
        val totalPrice = subTotal + DELIVERY_FEE + tax

        // Create order object
        val order = Order(
            id = "",
            customerId = customerId,
            orderCode = "",
            date = "2024-10-12T04:48:54.151Z",
            totalPrice = totalPrice,
            status = 0,
            orderItems = orderItems,
            orderItemCount = orderItems.size,
            isCancellationRequested = false,
            cancellationNote = "",
            isCancellationApproved = 0,
            recipientName = recipientName,
            recipientEmail = recipientEmail,
            recipientContact = recipientContact,
            recipientAddress = recipientAddress,
            customerFirstName = firstName,
            customerLastName = lastName
        )

        // Make API call to place the order
        val call = RetrofitClient.apiService.createOrder(order)

        call.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    showPaymentSuccessPopupBox()
                } else {
                    Toast.makeText(this@CheckoutActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    /*private fun createOrder() {
        // Get user session details
        val sharedPref: SharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        val customerId = sharedPref.getString("userId", "") ?: ""
        val firstName = sharedPref.getString("firstName", "") ?: ""
        val lastName = sharedPref.getString("lastName", "") ?: ""

        // Collect recipient details from the UI
        val recipientName = binding.OrderRecepName.text.toString().trim()
        val recipientEmail = binding.OrderRecepEmail.text.toString().trim()
        val recipientContact = binding.OrderRecepMobile.text.toString().trim()
        val recipientAddress = binding.OrderRecepAddress.text.toString().trim()

        // Validate recipient details
        if (recipientName.isEmpty()) {
            Toast.makeText(this, "Recipient Name is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (recipientEmail.isEmpty()) {
            Toast.makeText(this, "Recipient Email is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (recipientContact.isEmpty()) {
            Toast.makeText(this, "Recipient Contact is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (recipientAddress.isEmpty()) {
            Toast.makeText(this, "Recipient Address is required", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare the order items from the cart
        val orderItems = cartItems.map { item ->
            OrderItem(
                productId = item.id,
                productName = item.name,
                vendorId = item.productVendorName,
                unitPrice = item.price,
                quantity = checkoutCartAdapter.getQuantityForItem(item),
                total = item.price * checkoutCartAdapter.getQuantityForItem(item).toDouble(),
                isDelivered = false
            )
        }

        // Calculate total price
        val subTotal = calculateSubtotal()
        val tax = subTotal * TAX_RATE
        val totalPrice = subTotal + DELIVERY_FEE + tax

        // Create an order model
        val order = Order(
            id = "", // This will be generated by the server
            customerId = customerId,
            orderCode = "", // Auto-generated by the backend
            date = "2024-10-12T04:48:54.151Z", // Ideally, get the current timestamp programmatically
            totalPrice = totalPrice,
            status = 0, // Default status
            orderItems = orderItems,
            orderItemCount = orderItems.size,
            isCancellationRequested = false,
            cancellationNote = "",
            isCancellationApproved = 0,
            recipientName = recipientName,
            recipientEmail = recipientEmail,
            recipientContact = recipientContact,
            recipientAddress = recipientAddress,
            customerFirstName = firstName,
            customerLastName = lastName
        )

        val call = RetrofitClient.apiService.createOrder(order)

        call.enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    showPaymentSuccessPopupBox() // Show success dialog
                } else {
                    // Handle error
                    Toast.makeText(this@CheckoutActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@CheckoutActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun createOrder(order: Order) {
        val apiService = RetrofitClient.apiService
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createOrder(order)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        // Handle success, e.g., show a success message and navigate back to product list
                        showPaymentSuccessPopupBox()
                        Toast.makeText(this@CheckoutActivity, "Order Creation Successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle failure, e.g., show an error message
                        Toast.makeText(this@CheckoutActivity, "Order creation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle the exception, e.g., show a network error message
                    Toast.makeText(this@CheckoutActivity, "Network error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





}