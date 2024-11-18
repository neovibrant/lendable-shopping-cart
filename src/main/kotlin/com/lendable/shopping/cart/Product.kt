package com.lendable.shopping.cart

data class Product(
    val code: ProductCode,
    val name: String,
    val priceInPence: Int,
)
typealias ProductCode = String
