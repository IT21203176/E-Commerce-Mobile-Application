package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ecommerce_mobile_app.Adapter.BrandAdapter
import com.example.ecommerce_mobile_app.Adapter.SliderAdapter
import com.example.ecommerce_mobile_app.Model.BrandModel
import com.example.ecommerce_mobile_app.Model.SliderModel
import com.example.ecommerce_mobile_app.ViewModel.MainViewModel
import com.example.ecommerce_mobile_app.databinding.ActivityProductLactivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductLActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
            private lateinit var binding: ActivityProductLactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_lactivity)

        binding = ActivityProductLactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()

        initCategory()
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer { items->
            banners(items)
            binding.progressBarBanner.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(images:List<SliderModel>){
        binding.viewpagerSlider.adapter = SliderAdapter(images, binding.viewpagerSlider)
        binding.viewpagerSlider.clipToPadding = false
        binding.viewpagerSlider.clipChildren = false
        binding.viewpagerSlider.offscreenPageLimit = 3
        binding.viewpagerSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewpagerSlider.setPageTransformer(compositePageTransformer)
        if(images.size>1){
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewpagerSlider)
        }
    }

    private fun initCategory(){
        binding.progressBarCateg.visibility = View.VISIBLE
        viewModel.categories.observe(this, Observer {
            binding.viewCateg.layoutManager = LinearLayoutManager(this@ProductLActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCateg.adapter = BrandAdapter(it)
            binding.progressBarCateg.visibility = View.GONE
        })
        viewModel.loadCateogry()
    }

}