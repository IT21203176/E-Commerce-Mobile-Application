package com.example.ecommerce_mobile_app

import java.util.Date

/*data class ProductListsItem(
    val description: String,
    val id: String,
    val isActive: Boolean,
    val name: String

    /*val idCustomer: Int,
    val Full_Name: String,
    val City: String,
    val DOB: String,
    val TP: Int,
    val Email: String,
    val Address1: String,
    val Address2: String,
    val NIC: String,
    val discount_id: Int*/

)*/

data class ProductListsItem(
    val id: String,
    val name: String,
    val houseColours: String,
    val founder: String,
    val animal: String,
    val element: String,
    val ghost: String,
    val commonRoom: String,
    val heads: List<HouseHead>,
    val traits: List<Trait>
)

data class HouseHead(
    val id: String,
    val firstName: String,
    val lastName: String
)

data class Trait(
    val id: String,
    val name: String
)