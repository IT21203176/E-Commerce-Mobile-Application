package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.ecommerce_mobile_app.Adapter.BrandAdapter
import com.example.ecommerce_mobile_app.Adapter.PopProductAdapter
import com.example.ecommerce_mobile_app.Adapter.SliderAdapter
import com.example.ecommerce_mobile_app.Model.SliderModel
import com.example.ecommerce_mobile_app.ViewModel.MainViewModel
import com.example.ecommerce_mobile_app.databinding.ActivityProductLactivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

        initPopProduct()

        //getProductList()
    }

    /*private fun getProductList() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)

        val retrofitData = retrofitBuilder.getProductList()
        retrofitData.enqueue(object: Callback<List<ProductListsItem>?> {

            override fun onResponse(
                call: Call<List<ProductListsItem>?>,
                response: Response<List<ProductListsItem>?>
            ) {
                    /*val responseBody = response.body()!!

                    val myStringBuilder = StringBuilder()
                    for (categoryData in responseBody) {
                        myStringBuilder.append(categoryData.name)
                        myStringBuilder.append("\n")
                    }
                    binding.textView35.text = myStringBuilder*/

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    Log.d("ProductLActivity", "Response Body: $responseBody")

                    val myStringBuilder = StringBuilder()
                    for (categoryData in responseBody) {
                        myStringBuilder.append(categoryData.name)
                        myStringBuilder.append("\n")
                    }
                    binding.textView35.text = myStringBuilder
                } else {
                    Log.d("ProductLActivity", "Response Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ProductListsItem>?>, t: Throwable) {
                Log.d("ProductLActivity", "onFailure"+t.message)
            }

        })
    }*/

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

    /*private fun initCategory(){
        binding.progressBarCateg.visibility = View.VISIBLE
        viewModel.categories.observe(this, Observer {
            binding.viewCateg.layoutManager = LinearLayoutManager(this@ProductLActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCateg.adapter = BrandAdapter(it)
            binding.progressBarCateg.visibility = View.GONE
        })
        viewModel.loadCateogry()
    }*/



    private fun initCategory() {
        /*binding.progressBarCateg.visibility = View.VISIBLE


        // Observe the categories data from the ViewModel
        viewModel.categories.observe(this, Observer { categoryData ->
            // Set the data on textView35 (category name)
            binding.textView35.text = categoryData.toString()
            binding.progressBarCateg.visibility = View.GONE
        })

        // Fetch the categories (API call)
        viewModel.loadCategory() // You need to implement this function in your ViewModel*/
        binding.viewCateg.layoutManager = LinearLayoutManager(this@ProductLActivity, LinearLayoutManager.HORIZONTAL, false)

        viewModel.category.observe(this) { categoryList ->
            binding.viewCateg.adapter = BrandAdapter(categoryList.toMutableList())
        }

        binding.progressBarCateg.visibility = View.GONE
        viewModel.loadCategory()
    }

    private fun initPopProduct() {
        /*binding.progressBarCateg.visibility = View.VISIBLE


        // Observe the categories data from the ViewModel
        viewModel.categories.observe(this, Observer { categoryData ->
            // Set the data on textView35 (category name)
            binding.textView35.text = categoryData.toString()
            binding.progressBarCateg.visibility = View.GONE
        })

        // Fetch the categories (API call)
        viewModel.loadCategory() // You need to implement this function in your ViewModel*/
        binding.viewPopular.layoutManager = GridLayoutManager(this@ProductLActivity, 2)

        viewModel.popProducts.observe(this) { popProductList ->
            binding.viewPopular.adapter = PopProductAdapter(popProductList.toMutableList())
        }

        binding.progressBarProduct.visibility = View.GONE
        viewModel.loadPopular()
    }


}