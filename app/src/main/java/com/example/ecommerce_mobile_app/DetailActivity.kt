package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.databinding.ActivityDetailBinding
import com.example.ecommerce_mobile_app.databinding.ActivityProductLactivityBinding
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as ItemModel

        findViewById<TextView>(R.id.productTitle).text = product.name
        val decimalFormat = DecimalFormat("#,###.00")
        findViewById<TextView>(R.id.productPriceTxt).text = "LKR. ${decimalFormat.format(product.price)}"
        findViewById<TextView>(R.id.descriptionTxt).text = product.description

        val stockStatusTxt = findViewById<TextView>(R.id.stockStatusTxt)
        val stockStatusDot = findViewById<View>(R.id.stockStatusDot)


        if (product.stockStatus == "In Stock") {
            stockStatusTxt.text = "In Stock"
            stockStatusTxt.setTextColor(ContextCompat.getColor(this, R.color.green))
            stockStatusDot.setBackgroundResource(R.drawable.green_circle)
        } else {
            stockStatusTxt.text = "Out of Stock"
            stockStatusTxt.setTextColor(ContextCompat.getColor(this, R.color.red))
            stockStatusDot.setBackgroundResource(R.drawable.red_circle)
        }

        //val productImageView = findViewById<ViewPager2>(R.id.productImageView)
        // Load images here
        val requestOptions = RequestOptions().transform(CenterInside())
        Glide.with(this)
            .load(product.image)
            .apply(requestOptions)
            .into(binding.productImageView)

        // Handle back button
        findViewById<ImageView>(R.id.productBackBtn).setOnClickListener {
            finish()
        }
    }

}