package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Model.Order
import com.example.ecommerce_mobile_app.Model.OrderItem
import com.example.ecommerce_mobile_app.R

class OrderItemAdapter(
    private val context: Context,
    private val orderItems: List<OrderItem> // List of orders
) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    // ViewHolder class holding the views for a single item
    class OrderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.imageView26)
        val productName: TextView = itemView.findViewById(R.id.spec_o_product_name)
        val vendorName: TextView = itemView.findViewById(R.id.spec_o_product_v_name)
        val statusDescription: TextView = itemView.findViewById(R.id.ord_status_descrip)

        // Binds the data from the Order model to the UI components
        fun bind(order: OrderItem) {
            productName.text = order.productName
            vendorName.text = order.vendorId

            if (order.isDelivered) {
                statusDescription.text = "Delivered"
                statusDescription.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                statusDescription.setBackgroundResource(R.drawable.delivery_status_bg_green)
            } else {
                statusDescription.text = "Pending"
                statusDescription.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                statusDescription.setBackgroundResource(R.drawable.delivery_status_bg_yellow)
            }

            productImage.setImageResource(R.drawable.products)

        }
    }

    // Create the ViewHolder by inflating the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_product_n_vendor_wise_status, parent, false)
        return OrderItemViewHolder(view)
    }

    // Get the number of items in the list
    override fun getItemCount(): Int = orderItems.size

    // Bind data to the ViewHolder at a specific position
    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val order = orderItems[position]
        holder.bind(order)
    }
}
