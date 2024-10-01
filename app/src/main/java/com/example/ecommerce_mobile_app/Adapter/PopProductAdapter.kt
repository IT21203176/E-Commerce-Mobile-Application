package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.DetailActivity
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ViewholderRecommendedBinding
import java.text.DecimalFormat

class PopProductAdapter(val items: MutableList<ItemModel>):RecyclerView.Adapter<PopProductAdapter.Viewholder>() {

    private var context : Context? = null

    class Viewholder (val binding: ViewholderRecommendedBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopProductAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: PopProductAdapter.Viewholder, position: Int) {
        /*holder.binding.vholderTitleTxt.text = items[position].name
        holder.binding.vholderPriceTxt.text = "LKR. "+items[position].price.toString()
        holder.binding.vholderRatingTxt.text = items[position].stockStatus

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].image)
            .apply(requestOptions)
            .into(holder.binding.recProductImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("product", items[position])
            holder.itemView.context.startActivity(intent)
        }*/

        val product = items[position]

        // Set product name, price, and stock status in the ViewHolder
        holder.binding.vholderTitleTxt.text = product.name
        //holder.binding.vholderPriceTxt.text = "LKR. "+items[position].price.toString()
        val decimalFormat = DecimalFormat("#,###.00")
        holder.binding.vholderPriceTxt.text = "LKR. ${decimalFormat.format(items[position].price)}"
        holder.binding.vholderRatingTxt.text = product.stockStatus

        // Load product image using Glide
        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(product.image)
            .apply(requestOptions)
            .into(holder.binding.recProductImg)

        // Handle item click and navigate to DetailActivity, passing the selected product data
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("product", product) // Pass the product data
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}