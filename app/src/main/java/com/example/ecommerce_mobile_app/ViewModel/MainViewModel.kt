package com.example.ecommerce_mobile_app.ViewModel

import android.telecom.Call
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce_mobile_app.Model.ItemModel
import com.example.ecommerce_mobile_app.Model.SliderModel
import com.example.ecommerce_mobile_app.ProductListsItem
import com.example.ecommerce_mobile_app.RetrofitClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOError

class MainViewModel():ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _banner = MutableLiveData<List<SliderModel>>()
    val banners:LiveData<List<SliderModel>> = _banner

    private val _category = MutableLiveData<List<ProductListsItem>>()
    val category : LiveData<List<ProductListsItem>> = _category

    private val _popularProducts = MutableLiveData<List<ItemModel>>()
    val popProducts : LiveData<List<ItemModel>> = _popularProducts


    fun loadBanners(){
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun loadCategory() {
        /*viewModelScope.launch {
            try {
                // Fetch the product list from the API
                val productList = RetrofitClient.apiService.getProductList()

                // Build the string with the category names
                val categoryNames = productList.joinToString(separator = "\n") { it.name }

                // Update LiveData
                _category.value = categoryNames
            } catch (e: Exception) {
                // Handle any errors (e.g., show a message or log it)
                _category.value = "Failed to load categories"
            }
        }*/

        viewModelScope.launch {
            try {
                // Fetch the product list from the API
                val productList = RetrofitClient.apiService.getProductList()

                // Update LiveData with the product list
                _category.value = productList.toMutableList() // Keep it as MutableList<ProductListItem>
            } catch (e: Exception) {
                e.printStackTrace()

                // Set an empty list in case of failure
                _category.value = emptyList()
            }
        }
    }

    fun loadPopular() {

        /*viewModelScope.launch {
            try {
                // Fetch the product list from the API
                val popProductList = RetrofitClient.apiService.getPopProductList()

                // Update LiveData with the product list
                _popularProducts.value = popProductList.toMutableList() // Keep it as MutableList<ProductListItem>
            } catch (e: Exception) {
                e.printStackTrace()

                // Set an empty list in case of failure
                _popularProducts.value = emptyList()
            }
        }*/

        viewModelScope.launch {
            try {
                // Fetch the product list from the API
                val popProductList = RetrofitClient.apiService.getPopProductList()

                // Randomly select 4 products
                val randomPopularProducts = if (popProductList.size > 4) {
                    popProductList.shuffled().take(4)
                } else {
                    popProductList // If the list has less than or equal to 4 items, return the entire list
                }

                // Update LiveData with the randomly selected products
                _popularProducts.value = randomPopularProducts.toMutableList()
            } catch (e: Exception) {
                e.printStackTrace()

                // Set an empty list in case of failure
                _popularProducts.value = emptyList()
            }
        }
    }


}