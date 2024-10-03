package com.example.ecommerce_mobile_app.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.ProductListsItem
import com.example.ecommerce_mobile_app.RetrofitClient
import kotlinx.coroutines.launch

class CategoryProductViewModel : ViewModel() {

    // LiveData for product lists and products
    private val _productLists = MutableLiveData<List<ProductListsItem>>()
    val productLists: LiveData<List<ProductListsItem>> get() = _productLists

    private val _products = MutableLiveData<List<ItemModel>>()
    val products: LiveData<List<ItemModel>> get() = _products

    // Retrofit service instance
    private val apiService = RetrofitClient.apiService

    // Fetch product lists and products
    fun fetchProductLists() {
        viewModelScope.launch {
            try {
                val productListsResponse = apiService.getProductList()
                _productLists.value = productListsResponse
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchProductsByListName(listName: String) {
        viewModelScope.launch {
            try {
                val productsResponse = apiService.getProductsByList(listName)
                _products.value = productsResponse
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}