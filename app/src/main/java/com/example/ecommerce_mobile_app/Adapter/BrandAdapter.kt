package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ecommerce_mobile_app.ProductListsItem
import com.example.ecommerce_mobile_app.R
import com.example.ecommerce_mobile_app.databinding.ViewholderBrandBinding


class BrandAdapter (private val items: List<ProductListsItem>) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>(){

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class BrandViewHolder (val binding : ViewholderBrandBinding) :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrandAdapter.BrandViewHolder {
        context = parent.context
        val binding = ViewholderBrandBinding.inflate(LayoutInflater.from(context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandAdapter.BrandViewHolder, position: Int) {
        val item = items[position]

        holder.binding.viewholderBrandTitle.text = item.name
        holder.binding.viewholderBrandTitle.visibility = View.VISIBLE

        /*if (selectedPosition == position) {
            // Item is selected - set background to blue and text color to white
            holder.binding.root.setBackgroundResource(R.drawable.dark_moderate_blue_bg)  // Set your blue drawable
            holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(android.R.color.white))
        } else {
            // Reset to the default background and text color when not selected
            holder.binding.root.setBackgroundResource(R.drawable.grey_bg)  // Use the background you want for the unselected state
            holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(R.color.black))  // Default text color
        }

        // Handle item click to update the selected position
        holder.binding.root.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            // Notify adapter to refresh the UI for the previously selected and newly selected item
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }*/

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }

        holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(R.color.black))
        if (selectedPosition == position){
            holder.binding.mailLayout.setBackgroundResource(R.drawable.dark_moderate_blue_bg)
            holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(R.color.white))
            holder.binding.viewholderBrandTitle.visibility = View.VISIBLE
        } else {
            holder.binding.mailLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(R.color.black))
            holder.binding.viewholderBrandTitle.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int = items.size


}