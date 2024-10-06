package com.example.ecommerce_mobile_app

import com.example.ecommerce_mobile_app.Model.ItemModel

object Cart {

    private val cartItems = mutableListOf<ItemModel>()

    // Add an item to the cart
    fun addItem(item: ItemModel) {
        if (item.stock > 0) {
            cartItems.add(item)
        }
    }

    // Get all cart items
    fun getItems(): List<ItemModel> {
        return cartItems
    }

    // Remove an item (if needed)
    fun removeItem(item: ItemModel) {
        cartItems.remove(item)
    }

    // Clear the cart
    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.price.toDouble() } // Convert each Int price to Double
    }
}