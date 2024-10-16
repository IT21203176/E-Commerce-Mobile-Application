package com.example.ecommerce_mobile_app.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Model.Order
import com.example.ecommerce_mobile_app.OrderTrackingActivity
import com.example.ecommerce_mobile_app.R
import com.example.ecommerce_mobile_app.databinding.ViewholderMyOrdersBinding


class OrderAdapter(private val orderList: List<Order>,
                   private val onTrackButtonClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderIdTextView: TextView = view.findViewById(R.id.order_view_orderId)
        val recipientAddressTextView: TextView = view.findViewById(R.id.destination_location)
        val statusTextView: TextView = view.findViewById(R.id.textView51)
        val trackButton: Button = view.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_my_orders, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        // Bind order details to the view
        holder.orderIdTextView.text = order.orderCode
        holder.recipientAddressTextView.text = order.recipient_Address
        holder.statusTextView.text = when (order.status) {
            0 -> "Pending"
            1 -> "Processing"
            2 -> "Delivered"
            3 -> "Canceled"
            4 -> "Cancel Requested"
            else -> "Unknown"
        }

        when (order.status) {
            0 -> {
                holder.statusTextView.setBackgroundResource(R.drawable.delivery_status_bg_yellow)
                holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
            }
            1 -> {
                holder.statusTextView.setBackgroundResource(R.drawable.delivery_status_bg_blue)
                holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.dark_moderate_blue))
            }
            2 -> {
                holder.statusTextView.setBackgroundResource(R.drawable.delivery_status_bg_green)
                holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            }
            3 -> {
                holder.statusTextView.setBackgroundResource(R.drawable.delivery_status_bg_red)
                holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            }
            4 -> {
                holder.statusTextView.setBackgroundResource(R.drawable.delivery_status_bg_pink)
                holder.statusTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pink))
            }
        }

        holder.trackButton.setOnClickListener {
            // Create an Intent to navigate to OrderTrackingActivity
            val context = holder.itemView.context
            val intent = Intent(context, OrderTrackingActivity::class.java)
            intent.putExtra("ORDER_ID", order.id) // Pass the order ID or any relevant data
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orderList.size
}
