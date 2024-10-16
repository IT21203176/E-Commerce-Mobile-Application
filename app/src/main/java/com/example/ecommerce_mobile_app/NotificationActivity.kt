package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.NotificationAdapter
import com.example.ecommerce_mobile_app.Model.NotificationModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private val notificationList: MutableList<NotificationModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        recyclerView = findViewById(R.id.viewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val backButton: ImageView = findViewById(R.id.imageView13)
        backButton.setOnClickListener {
            finish()
        }

        notificationAdapter = NotificationAdapter(notificationList) { notification ->
            deleteNotification(notification)
        }
        recyclerView.adapter = notificationAdapter

        // Fetch the receiverId from SharedPreferences
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val receiverId = sharedPref.getString("userId", null)

        if (receiverId != null) {
            getNotifications(receiverId)
        } else {
            Toast.makeText(this, "Receiver ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNotifications(receiverId: String) {
        // Use RetrofitClient directly to access the apiService
        RetrofitClient.apiService.getNotifications(receiverId).enqueue(object : Callback<List<NotificationModel>> {
            override fun onResponse(call: Call<List<NotificationModel>>, response: Response<List<NotificationModel>>) {
                if (response.isSuccessful && response.body() != null) {
                    notificationList.clear()
                    notificationList.addAll(response.body()!!)
                    notificationAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@NotificationActivity, "Failed to load notifications", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<NotificationModel>>, t: Throwable) {
                Log.e("NotificationActivity", "Error: ${t.message}")
                Toast.makeText(this@NotificationActivity, "Error loading notifications", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun deleteNotification(notification: NotificationModel) {
        Toast.makeText(this, "Delete action for notification: ${notification.message}", Toast.LENGTH_SHORT).show()
    }
}