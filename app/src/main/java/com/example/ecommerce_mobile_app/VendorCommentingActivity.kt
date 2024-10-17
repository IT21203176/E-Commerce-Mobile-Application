package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.CommentAdapter
import com.example.ecommerce_mobile_app.Model.Comments
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorCommentingActivity : AppCompatActivity() {

    private lateinit var commentEditText: EditText
    private lateinit var submitCommentBtn: MaterialButton
    private lateinit var commentsAdapter: CommentAdapter
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsList: MutableList<Comments>

    private lateinit var customerId: String
    private lateinit var customerName: String

    private lateinit var vendorId: String
    private var editingCommentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_commenting)

        commentEditText = findViewById(R.id.commentEditText)
        submitCommentBtn = findViewById(R.id.submitCommentBtn)
        commentsRecyclerView = findViewById(R.id.customerCommentsRv)

        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsList = mutableListOf()
        commentsAdapter = CommentAdapter(commentsList) { comment ->
            showEditDialog(comment)
        }
        commentsRecyclerView.adapter = commentsAdapter

        vendorId = intent.getStringExtra("productVendorId").orEmpty()

        if (vendorId.isEmpty()) {
            Toast.makeText(this, "Vendor ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        retrieveCustomerDetails()

        submitCommentBtn.setOnClickListener {
            val commentText = commentEditText.text.toString()
            if (commentText.isNotBlank()) {
                if (editingCommentId != null) {
                    updateComment(editingCommentId!!, commentText)
                } else {
                    addComment(commentText)
                }
            } else {
                Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        loadComments()

        findViewById<ImageView>(R.id.commentBackBtn).setOnClickListener {
            finish()
        }
    }

    private fun retrieveCustomerDetails() {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        customerId = sharedPref.getString("userId", "").orEmpty()
        customerName = sharedPref.getString("firstName", "").orEmpty()

        if (customerId.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "User session not found. Please log in again.", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@VendorCommentingActivity, "Comment added successfully", Toast.LENGTH_SHORT).show()
                    commentEditText.text.clear()
                    loadComments()
                } else {
                    Toast.makeText(this@VendorCommentingActivity, "Failed to add comment", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VendorCommentingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadComments() {
        RetrofitClient.apiService.getComments(vendorId).enqueue(object : Callback<List<Comments>> {
            override fun onResponse(call: Call<List<Comments>>, response: Response<List<Comments>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        commentsList.clear()
                        commentsList.addAll(it)
                        commentsAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@VendorCommentingActivity, "Failed to load comments", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Toast.makeText(this@VendorCommentingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditDialog(comment: Comments) {
        editingCommentId = comment.id
        commentEditText.setText(comment.comment)
        submitCommentBtn.text = "Update Comment"
    }

    private fun updateComment(commentId: String, newCommentText: String) {
        val updatedComment = Comments(
            id = commentId,
            customerId = customerId,
            vendorId = vendorId,
            comment = newCommentText,
            customerName = customerName
        )

        RetrofitClient.apiService.editComment(commentId, updatedComment).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VendorCommentingActivity, "Comment updated successfully", Toast.LENGTH_SHORT).show()
                    editingCommentId = null
                    commentEditText.text.clear()
                    submitCommentBtn.text = "Submit Comment"
                    loadComments()
                } else {
                    Toast.makeText(this@VendorCommentingActivity, "Failed to update comment", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@VendorCommentingActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
