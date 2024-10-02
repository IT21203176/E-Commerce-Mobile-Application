package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.Cart
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ViewholderCartBinding
import com.example.project1762.Helper.ChangeNumberItemsListener
import java.text.DecimalFormat

class CartAdapter(val listItemSelected: ArrayList<ItemModel>, context: Context, var changeNumberItemsListner: ChangeNumberItemsListener? = null) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    // Quantity map to store the quantity for each item by its position in the list
    private val quantityMap = mutableMapOf<Int, Int>()

    init {
        // Initialize all quantities to 1 by default
        listItemSelected.forEachIndexed { index, _ -> quantityMap[index] = 1 }
    }

    class ViewHolder (val binding: ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item = listItemSelected[position]
        val decimalFormat = DecimalFormat("#,###.00")

        // Get the current quantity from the map
        val currentQuantity = quantityMap[position] ?: 1

        holder.binding.titleText.text = item.name
        holder.binding.eachItemFee.text = "LKR. ${decimalFormat.format(item.price)}"
        holder.binding.eachItemTotal.text = "LKR ${decimalFormat.format(currentQuantity * item.price)}"
        holder.binding.numOfItems.text = currentQuantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.image)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.itemImage)

        holder.binding.clearCartBtn.setOnClickListener {
            // Remove the item from the list and notify the adapter
            removeItem(position)
        }

        holder.binding.plusCartBtn.setOnClickListener {

                // Fetch the current quantity from the map again to avoid stale data
                val updatedQuantity = (quantityMap[position] ?: 1) + 1
                quantityMap[position] = updatedQuantity

                // Update the views with new quantity
                holder.binding.numOfItems.text = updatedQuantity.toString()
                holder.binding.eachItemTotal.text = "LKR ${decimalFormat.format(updatedQuantity * item.price)}"

                // Notify the listener and refresh the item view
                changeNumberItemsListner?.onChanged()
                notifyItemChanged(position)
        }

        holder.binding.minusCartBtn.setOnClickListener {
            /*if (currentQuantity > 1) {
                val newQuantity = currentQuantity - 1
                quantityMap[position] = newQuantity
                holder.binding.numOfItems.text = newQuantity.toString()
                holder.binding.eachItemTotal.text = "LKR ${decimalFormat.format(newQuantity * item.price)}"

                changeNumberItemsListner?.onChanged()
            }*/
            // Fetch the current quantity from the map again to avoid stale data
            val updatedQuantity = (quantityMap[position] ?: 1) - 1
            if (updatedQuantity > 0) { // Prevent going below 1
                quantityMap[position] = updatedQuantity

                // Update the views with the new quantity
                holder.binding.numOfItems.text = updatedQuantity.toString()
                holder.binding.eachItemTotal.text = "LKR ${decimalFormat.format(updatedQuantity * item.price)}"

                // Notify the listener and refresh the item view
                changeNumberItemsListner?.onChanged()
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size

    fun getQuantityForItem(item: ItemModel): Int {
        val position = listItemSelected.indexOf(item)
        return quantityMap[position] ?: 1
    }

    private fun removeItem(position: Int) {
        // Get the item to be removed
        val itemToRemove = listItemSelected[position]

        // Remove the item from the list and notify the adapter
        listItemSelected.removeAt(position)
        quantityMap.remove(position)

        // Update the Cart object
        Cart.removeItem(itemToRemove)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listItemSelected.size)
        changeNumberItemsListner?.onChanged() // Notify the listener to update the total price
    }
}