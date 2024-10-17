package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.ecommerce_mobile_app.Model.RankingComments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RateVendorActivity : AppCompatActivity() {

    private var userRate: Float = 0f

    private lateinit var ratingBar: RatingBar
    private lateinit var vendorId: String
    private lateinit var customerId: String
    private lateinit var customerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_vendor)

        val rateNowBtn = findViewById<AppCompatButton>(R.id.rateNowBtn)
        ratingBar = findViewById(R.id.ratingBar)
        val ratingImage = findViewById<ImageView>(R.id.ratingImage)

        // Assign vendorId to the class-level variable
        vendorId = intent.getStringExtra("productVendorId").orEmpty()

        if (vendorId.isEmpty()) {
            Toast.makeText(this, "Vendor ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve customer details from shared preferences
        retrieveCustomerDetails()

        // Set listener for the rating bar
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            userRate = rating
            when {
                rating <= 1 -> ratingImage.setImageResource(R.drawable.one_star)
                rating <= 2 -> ratingImage.setImageResource(R.drawable.two_star)
                rating <= 3 -> ratingImage.setImageResource(R.drawable.three_star)
                rating <= 4 -> ratingImage.setImageResource(R.drawable.four_star)
                rating <= 5 -> ratingImage.setImageResource(R.drawable.five_star)
            }
        }

        rateNowBtn.setOnClickListener {
            submitRating()
        }
    }

    private fun retrieveCustomerDetails() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        customerId = sharedPref.getString("userId", "").orEmpty()
        customerName = sharedPref.getString("firstName", "").orEmpty()

        // Log the retrieved values
        Log.d("RateVendorActivity", "Retrieved customerId: $customerId")
        Log.d("RateVendorActivity", "Retrieved customerName: $customerName")

        if (customerId.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "User session not found. Please log in again.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun submitRating() {
        // Validate user rating
        if (userRate == 0f) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val rankingComments = RankingComments(
                id = "",
                customerId = customerId,
                vendorId = vendorId,
                ranking = userRate.toInt(),
                customerName = customerName
            )

            try {
                val rankingResponse = RetrofitClient.apiService.postRanking(rankingComments)
                withContext(Dispatchers.Main) {
                    if (rankingResponse.isSuccessful) {
                        Log.d("RateVendorActivity", "Ranking submitted successfully!")
                        Toast.makeText(this@RateVendorActivity, "Rating submitted successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after successful submission
                    } else {
                        Log.e("RateVendorActivity", "Ranking response failed: ${rankingResponse.errorBody()}")
                        Toast.makeText(this@RateVendorActivity, "Failed to submit rating", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RateVendorActivity", "Error occurred while submitting rating: ${e.message}")
                    Toast.makeText(this@RateVendorActivity, "Error submitting rating", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
