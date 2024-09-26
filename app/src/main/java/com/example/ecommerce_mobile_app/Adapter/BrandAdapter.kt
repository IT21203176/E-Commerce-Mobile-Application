package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Model.BrandModel
import com.example.ecommerce_mobile_app.R
import com.example.ecommerce_mobile_app.databinding.ViewholderBrandBinding

class BrandAdapter (val items: MutableList<BrandModel>):
    RecyclerView.Adapter<BrandAdapter.Viewholder>(){

        private var selectedPosition = -1
        private var lastSelectedPosition = -1
        private lateinit var context: Context

        class Viewholder (val binding: ViewholderBrandBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderBrandBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BrandAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.viewholderBrandTitle.text = item.name
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        holder.binding.viewholderBrandTitle.setTextColor(context.resources.getColor(R.color.dark_moderate_blue))
        if (selectedPosition == position){
            holder.binding.mailLayout.setBackgroundColor(R.drawable.dark_moderate_blue_bg)
        }else{
            holder.binding.mailLayout.setBackgroundColor(R.drawable.light_blue_bg)
        }
    }

    override fun getItemCount(): Int = items.size

}