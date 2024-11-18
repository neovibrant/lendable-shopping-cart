package com.lendable.shopping.cart

data class ShoppingCartItem(val product: Product, val quantity: Int) {
    val priceInPence: Int by lazy { product.priceInPence * quantity }
}
