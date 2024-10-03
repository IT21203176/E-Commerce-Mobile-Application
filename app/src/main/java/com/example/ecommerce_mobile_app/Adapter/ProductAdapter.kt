package com.example.ecommerce_mobile_app.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.DetailActivity
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ViewholderCategoryProductBinding
import java.text.DecimalFormat

class ProductAdapter(private val items: List<ItemModel>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val binding: ViewholderCategoryProductBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAdapter.ProductViewHolder {
        val binding = ViewholderCategoryProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val item = items[position]

        val decimalFormat = DecimalFormat("#,###.00")

        holder.binding.vholderTitleTxt.text = item.name
        holder.binding.vholderPriceTxt.text = "LKR. ${decimalFormat.format(items[position].price)}"
        holder.binding.vholderRatingTxt.text = item.stockStatus

        // Load product image using Glide
        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.image)
            .apply(requestOptions)
            .into(holder.binding.categProductImg)

        // Handle item click and navigate to DetailActivity, passing the selected product data
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("product", item) // Pass the product data
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}