package com.example.ecommerce_mobile_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_mobile_app.Adapter.ProductAdapter
import com.example.ecommerce_mobile_app.ViewModel.CategoryProductViewModel
import com.example.ecommerce_mobile_app.databinding.ActivityCategoryProductBinding

class CategoryProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryProductBinding
    private lateinit var productAdapter: ProductAdapter
    private val viewModel: CategoryProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_product)

        binding = ActivityCategoryProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.viewProducts)
        val gridLayoutManager = GridLayoutManager(this, 2) // Set span count to 2
        recyclerView.layoutManager = gridLayoutManager


        // Get the category name from the Intent
        val categoryName = intent.getStringExtra("category_name")
        val categoryNameTextView: TextView = findViewById(R.id.productCategName)
        categoryNameTextView.text = categoryName

        // Initialize the adapter with an empty list and set it to RecyclerView
        productAdapter = ProductAdapter(emptyList()) // Only pass the list of items
        recyclerView.adapter = productAdapter

        // Observe ViewModel data and update the adapter when data is loaded
        /*viewModel.products.observe(this) { products ->
            productAdapter = ProductAdapter(products) // Pass the list of products
            recyclerView.adapter = productAdapter
        }*/

        viewModel.products.observe(this) { products ->
            // Filter products based on the selected category
            val filteredProducts = products.filter { product ->
                product.productListName == categoryName // Assuming productListName matches the category name
            }

            // Update the adapter with the filtered list
            productAdapter = ProductAdapter(filteredProducts)
            recyclerView.adapter = productAdapter
        }

        // Fetch products for the selected category
        if (categoryName != null) {
            viewModel.fetchProductsByListName(categoryName)
        }

        // Handle back button click
        val backButton: ImageView = findViewById(R.id.productsBackBtn)
        backButton.setOnClickListener {
            finish() // Navigate back to the previous screen
        }
    }
}