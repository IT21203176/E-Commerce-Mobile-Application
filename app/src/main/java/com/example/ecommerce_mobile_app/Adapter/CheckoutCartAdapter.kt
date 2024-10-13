package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.Cart
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ViewholderOrderCheckoutBinding
import com.example.project1762.Helper.ChangeNumberItemsListener
import java.text.DecimalFormat

class CheckoutCartAdapter(val listItemSelected: ArrayList<ItemModel>, context: Context, var changeNumberItemsListner: ChangeNumberItemsListener? = null) : RecyclerView.Adapter<CheckoutCartAdapter.ViewHolder>() {

    // Quantity map to store the quantity for each item by its position in the list
    private val quantityMap = mutableMapOf<Int, Int>()

    init {
        // Initialize all quantities to 1 by default
        listItemSelected.forEachIndexed { index, _ -> quantityMap[index] = 1 }
    }

    class ViewHolder (val binding : ViewholderOrderCheckoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutCartAdapter.ViewHolder {
        val binding = ViewholderOrderCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listItemSelected.size

    override fun onBindViewHolder(holder: CheckoutCartAdapter.ViewHolder, position: Int) {
        val item = listItemSelected[position]
        val decimalFormat = DecimalFormat("#,###.00")

        // Get the current quantity from the map
        val currentQuantity = quantityMap[position] ?: 1

        holder.binding.orderTitleText.text = item.name
        holder.binding.orderEachItemFee.text = "LKR. ${decimalFormat.format(item.price)}"
        holder.binding.orderEachItemTotal.text = "LKR ${decimalFormat.format(currentQuantity * item.price)}"
        holder.binding.orderNumOfItems.text = currentQuantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.image)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.orderItemImage)

        holder.binding.orderClearCartBtn.setOnClickListener {
            // Remove the item from the list and notify the adapter
            removeItem(position)
        }

        holder.binding.orderPlusCartBtn.setOnClickListener {

            // Fetch the current quantity from the map again to avoid stale data
            val updatedQuantity = (quantityMap[position] ?: 1) + 1
            quantityMap[position] = updatedQuantity

            // Update the views with new quantity
            holder.binding.orderNumOfItems.text = updatedQuantity.toString()
            holder.binding.orderEachItemTotal.text = "LKR ${decimalFormat.format(updatedQuantity * item.price)}"

            // Notify the listener and refresh the item view
            changeNumberItemsListner?.onChanged()
            notifyItemChanged(position)
        }

        holder.binding.orderMinusCartBtn.setOnClickListener {
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
                holder.binding.orderNumOfItems.text = updatedQuantity.toString()
                holder.binding.orderEachItemTotal.text = "LKR ${decimalFormat.format(updatedQuantity * item.price)}"

                // Notify the listener and refresh the item view
                changeNumberItemsListner?.onChanged()
                notifyItemChanged(position)
            }
        }
    }

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

    fun getQuantityMap(): HashMap<Int, Int> {
        return HashMap(quantityMap) // Return a serializable map
    }
}