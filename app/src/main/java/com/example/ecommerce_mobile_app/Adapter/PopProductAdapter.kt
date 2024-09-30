package com.example.ecommerce_mobile_app.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ViewholderRecommendedBinding

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
        holder.binding.vholderTitleTxt.text = items[position].name

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].photo)
            .apply(requestOptions)
            .into(holder.binding.recProductImg)

        /*holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context)
        }*/
    }

    override fun getItemCount(): Int = items.size
}