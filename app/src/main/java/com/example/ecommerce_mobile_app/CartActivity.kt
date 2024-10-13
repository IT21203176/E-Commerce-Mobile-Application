package com.example.ecommerce_mobile_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_mobile_app.Adapter.CartAdapter
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ActivityCartBinding
import com.example.project1762.Helper.ChangeNumberItemsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val selectedItems = ArrayList<ItemModel>()
    private lateinit var cartItems: ArrayList<ItemModel>

    companion object {
        const val DELIVERY_FEE = 300.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchCartItems()
        cartItems = getCartItems()

        binding.checkoutBtn.setOnClickListener {
            // Handle checkout button click
            // Handle checkout button click
            if (selectedItems.isNotEmpty()) {
                // Navigate to CheckoutActivity
                val intent = Intent(this, CheckoutActivity::class.java)

                // Pass the cart details as extras
                intent.putExtra("SUB_TOTAL", calculateSubtotal())
                intent.putExtra("DELIVERY_FEE", DELIVERY_FEE)
                intent.putExtra("TAX", calculateTax())
                intent.putExtra("TOTAL", calculateTotal())
                // Pass the cart items
                intent.putExtra("CART_ITEMS", selectedItems)

                startActivity(intent) // Start the CheckoutActivity
            } else {
                Toast.makeText(this, "No items in the cart", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish() // Close the cart activity and go back to the previous screen
        }
    }

    private fun fetchCartItems() {
        binding.emptyText.visibility = View.GONE
        binding.viewCart.visibility = View.GONE

        /*CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.getPopProductList() // Fetch data from the API
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        selectedItems.clear()
                        selectedItems.addAll(response)
                        cartAdapter.notifyDataSetChanged()
                        binding.viewCart.visibility = View.VISIBLE
                        calculateCartTotal()
                    } else {
                        binding.emptyText.visibility = View.VISIBLE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Failed to load cart items", Toast.LENGTH_SHORT).show()
                }
            }
        }*/

        // Fetch the items from the Cart object instead of an API
        selectedItems.clear()
        selectedItems.addAll(Cart.getItems()) // Fetch items from the cart

        if (selectedItems.isNotEmpty()) {
            cartAdapter.notifyDataSetChanged()
            binding.viewCart.visibility = View.VISIBLE
            calculateCartTotal()
        } else {
            binding.emptyText.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(selectedItems, this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCartTotal()
            }
        })
        binding.viewCart.layoutManager = LinearLayoutManager(this)
        binding.viewCart.adapter = cartAdapter
    }

    private fun calculateCartTotal() {
        /*var subtotal = 0
        for (item in selectedItems) {
            subtotal += item.price
        }*/

        var subTotal = 0.0
        for (item in cartAdapter.listItemSelected) {
            // Multiply the item's price by its quantity in the quantityMap
            val quantity = cartAdapter.getQuantityForItem(item) // Add this method in CartAdapter to fetch the quantity
            subTotal += item.price * quantity
        }

        val deliveryFee = 300
        val tax = (subTotal * 0.05).toInt()
        val total = subTotal + deliveryFee + tax

        val decimalFormat = DecimalFormat("#,###.00")
        binding.totalFeeTxt.text = "LKR. ${decimalFormat.format(subTotal)}"
        binding.deliveryFeeTxt.text = "LKR. ${decimalFormat.format(deliveryFee)}"
        binding.taxTxt.text = "LKR. ${decimalFormat.format(tax)}"
        binding.TotalTxt.text = "LKR. ${decimalFormat.format(total)}"
    }

    private fun calculateSubtotal(): Double {
        var subTotal = 0.0
        for (item in cartAdapter.listItemSelected) {
            val quantity = cartAdapter.getQuantityForItem(item) // Get quantity for the item
            subTotal += item.price * quantity
        }
        return subTotal
    }

    private fun calculateTax(): Double {
        val subTotal = calculateSubtotal()
        return (subTotal * 0.05) // Example tax (5%)
    }

    private fun calculateTotal(): Double {
        val subTotal = calculateSubtotal()
        val deliveryFee = 300.0 // Example delivery fee
        val tax = calculateTax()
        return subTotal + deliveryFee + tax
    }

    private fun getCartItems(): ArrayList<ItemModel> {
        // This should be replaced by the actual logic to fetch the selected items in the cart
        return ArrayList()
    }

}