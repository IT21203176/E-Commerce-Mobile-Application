package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_mobile_app.Model.Comments
import com.example.ecommerce_mobile_app.Model.RankingComments
import com.example.ecommerce_mobile_app.Model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class RateVendorActivity : AppCompatActivity() {

    private var userRate: Float = 0f

    private lateinit var etRemarks: EditText
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
        etRemarks = findViewById(R.id.etRemarks)

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
            submitRatingAndComment()
        }
    }

    /*private fun retrieveCustomerDetails() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        customerId = sharedPref.getString("userId", "").orEmpty()
        customerName = sharedPref.getString("firstName", "").orEmpty()

        // Log the retrieved values
        Log.d("RateVendorActivity", "Retrieved customerId: $customerId")
        Log.d("RateVendorActivity", "Retrieved customerName: $customerName")

        if (customerId.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "User session not found. Please log in again.", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if session details are missing
        }
    }

    private fun submitRatingAndComment() {
        val commentText = etRemarks.text.toString().trim()
        if (userRate == 0f) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            return
        }

        // Post the ranking and comment
        CoroutineScope(Dispatchers.IO).launch {
            val rankingComments = RankingComments(
                id = "",
                customerId = customerId,
                vendorId = vendorId,
                ranking = userRate.toInt(),
                customerName = customerName
            )

            val comments = Comments(
                id = "",
                customerId = customerId,
                vendorId = vendorId,
                comment = commentText,
                customerName = customerName
            )

            try {
                val rankingResponse = RetrofitClient.apiService.postRanking(rankingComments)
                val commentResponse = RetrofitClient.apiService.postComment(comments)

                withContext(Dispatchers.Main) {
                    if (rankingResponse.isSuccessful && commentResponse.isSuccessful) {
                        Toast.makeText(this@RateVendorActivity, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after submission
                    } else {
                        Toast.makeText(this@RateVendorActivity, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RateVendorActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/

    private fun retrieveCustomerDetails() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        customerId = sharedPref.getString("userId", "").orEmpty()
        customerName = sharedPref.getString("firstName", "").orEmpty()

        // Log the retrieved values
        Log.d("RateVendorActivity", "Retrieved customerId: $customerId")
        Log.d("RateVendorActivity", "Retrieved customerName: $customerName")

        if (customerId.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "User session not found. Please log in again.", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if session details are missing
        }
    }

    /*private fun submitRatingAndComment() {
        val commentText = etRemarks.text.toString().trim()
        if (userRate == 0f) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            return
        }

        // Post the ranking and comment
        CoroutineScope(Dispatchers.IO).launch {
            val rankingComments = RankingComments(
                id = "",
                customerId = customerId,
                vendorId = vendorId, // Use the class-level vendorId here
                ranking = userRate.toInt(),
                customerName = customerName
            )

            val comments = Comments(
                id = "",
                customerId = customerId,
                vendorId = vendorId, // Use the class-level vendorId here
                comment = commentText,
                customerName = customerName
            )

            try {
                val rankingResponse = RetrofitClient.apiService.postRanking(rankingComments)
                val commentResponse = RetrofitClient.apiService.postComment(comments)

                withContext(Dispatchers.Main) {
                    if (rankingResponse.isSuccessful && commentResponse.isSuccessful) {
                        Log.d("RateVendorActivity", "Ranking response: ${rankingResponse.body()}")
                        Log.d("RateVendorActivity", "Comment response: ${commentResponse.body()}")

                        Toast.makeText(this@RateVendorActivity, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after submission
                    } else {
                        Log.e("RateVendorActivity", "Ranking response failed: ${rankingResponse.errorBody()}")
                        Log.e("RateVendorActivity", "Comment response failed: ${commentResponse.errorBody()}")
                        Toast.makeText(this@RateVendorActivity, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RateVendorActivity", "Error occurred: ${e.message}")
                    Toast.makeText(this@RateVendorActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }*/

    private fun submitRatingAndComment() {
        val commentText = etRemarks.text.toString().trim()

        // Validate user rating
        if (userRate == 0f) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show()
            return
        }

        // Submit ranking
        submitRating { rankingSuccess ->
            if (rankingSuccess) {
                // Submit comment only if ranking was successful
                submitComment(commentText)
            } else {
                Toast.makeText(this, "Failed to submit rating", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun submitRating(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val rankingComments = RankingComments(
                id = "", // Generate a new ID
                customerId = customerId,
                vendorId = vendorId,
                ranking = userRate.toInt(),
                customerName = customerName
            )

            try {
                val rankingResponse = RetrofitClient.apiService.postRanking(rankingComments)
                withContext(Dispatchers.Main) {
                    if (rankingResponse.isSuccessful) {
                        Log.d("RateVendorActivity", "Ranking response: ${rankingResponse.body()}")
                        callback(true) // Notify success
                    } else {
                        Log.e("RateVendorActivity", "Ranking response failed: ${rankingResponse.errorBody()}")
                        callback(false) // Notify failure
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RateVendorActivity", "Error occurred while submitting rating: ${e.message}")
                    callback(false) // Notify failure
                }
            }
        }
    }

    private fun submitComment(commentText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val comments = Comments(
                id = "", // Generate a new ID
                customerId = customerId,
                vendorId = vendorId,
                comment = commentText,
                customerName = customerName
            )

            try {
                val commentResponse = RetrofitClient.apiService.postComment(comments)
                withContext(Dispatchers.Main) {
                    if (commentResponse.isSuccessful) {
                        Log.d("RateVendorActivity", "Comment response: ${commentResponse.body()}")
                        Toast.makeText(this@RateVendorActivity, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after submission
                    } else {
                        Log.e("RateVendorActivity", "Comment response failed: ${commentResponse.errorBody()}")
                        Toast.makeText(this@RateVendorActivity, "Failed to submit comment", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("RateVendorActivity", "Error occurred while submitting comment: ${e.message}")
                    Toast.makeText(this@RateVendorActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}