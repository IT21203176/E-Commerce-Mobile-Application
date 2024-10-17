package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.CommentAdapter
import com.example.ecommerce_mobile_app.Adapter.OrderItemAdapter
import com.example.ecommerce_mobile_app.Model.Comments
import com.example.ecommerce_mobile_app.Model.Order
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorCommentingActivity : AppCompatActivity() {

    private lateinit var commentEditText: EditText
    private lateinit var submitCommentBtn: MaterialButton
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentAdapter
    private lateinit var commentsList: MutableList<Comments>

    private lateinit var customerId: String
    private lateinit var customerName: String

    private lateinit var vendorId: String
    private lateinit var comment: Comments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_commenting)

        // Initialize views
        commentEditText = findViewById(R.id.commentEditText)
        submitCommentBtn = findViewById(R.id.submitCommentBtn)
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView)

        // Set up RecyclerView
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsList = mutableListOf()
        commentsAdapter = CommentAdapter(commentsList)
        commentsRecyclerView.adapter = commentsAdapter

        // Assign vendorId to the class-level variable
        vendorId = intent.getStringExtra("productVendorId").orEmpty()

        if (vendorId.isEmpty()) {
            Toast.makeText(this, "Vendor ID not found", Toast.LENGTH_SHORT).show()
            return
        }


        // Retrieve customer details
        retrieveCustomerDetails()

        // Set click listener for submit button
        submitCommentBtn.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotBlank()) {
                addComment(commentText)
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        // Load existing comments
        loadComments()

        findViewById<ImageView>(R.id.commentBackBtn).setOnClickListener {
            finish()
        }
    }

    private fun retrieveCustomerDetails() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        customerId = sharedPref.getString("userId", "").orEmpty()
        customerName = sharedPref.getString("firstName", "").orEmpty()

        Log.d("VendorCommentingActivity", "Retrieved customerId: $customerId")
        Log.d("VendorCommentingActivity", "Retrieved customerName: $customerName")

        if (customerId.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "User session not found. Please log in again.", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun addComment(commentText: String) {
        val comment = Comments(
            id = "",
            customerId = customerId,
            vendorId = vendorId,
            comment = commentText,
            customerName = customerName
        )

        RetrofitClient.apiService.addComment(comment).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@VendorCommentingActivity,
                        "Comment added successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    commentEditText.text.clear()
                    loadComments()
                } else {
                    Toast.makeText(
                        this@VendorCommentingActivity,
                        "Failed to add comment",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    this@VendorCommentingActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /*private fun loadComments() {
        RetrofitClient.apiService.getComments(vendorId).enqueue(object : Callback<List<Comments>> {
            override fun onResponse(call: Call<List<Comments>>, response: Response<List<Comments>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        commentsList.clear() // Clear existing comments
                        commentsList.addAll(it) // Add new comments
                        commentsAdapter.notifyDataSetChanged() // Notify adapter of data change
                    }
                } else {
                    Toast.makeText(this@VendorCommentingActivity, "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Toast.makeText(this@VendorCommentingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
    private fun loadComments() {
        RetrofitClient.apiService.getComments(vendorId).enqueue(object : Callback<List<Comments>> {
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        commentsList.clear() // Clear existing comments
                        commentsList.addAll(it) // Add new comments

                        // Set up CommentAdapter and bind it to the RecyclerView
                        val commentItemAdapter = CommentAdapter(commentsList)
                        commentsRecyclerView.adapter = commentItemAdapter

                        // Notify adapter of data change
                        commentItemAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@VendorCommentingActivity,
                        "Failed to load comments",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Toast.makeText(
                    this@VendorCommentingActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
