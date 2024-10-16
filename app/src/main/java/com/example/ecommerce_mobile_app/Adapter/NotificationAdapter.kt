package com.example.ecommerce_mobile_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Model.NotificationModel
import com.example.ecommerce_mobile_app.R

class NotificationAdapter(
    private val notifications: List<NotificationModel>,
    private val onDeleteClick: (NotificationModel) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.tvMessage)
        val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
        val deleteImageView: ImageView = itemView.findViewById(R.id.imageView14)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.NotificationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_notification, parent, false)
        return NotificationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: NotificationAdapter.NotificationViewHolder,
        position: Int
    ) {
        val notification = notifications[position]
        holder.messageTextView.text = notification.message
        holder.dateTextView.text = notification.createdAt.substring(0, 10)  // Display only the date

        // Set the delete button click action
        holder.deleteImageView.setOnClickListener {
            onDeleteClick(notification)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}