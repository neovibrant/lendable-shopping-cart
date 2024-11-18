package com.lendable.shopping.cart

data class ShoppingCartOffer(val offer: Offer, val valueInPence: Int, val quantity: Int) {
    val priceInPence: Int by lazy { valueInPence * quantity }
}
