package com.lendable.shopping.cart

data class Receipt(
    val items: List<ShoppingCartItem>,
    val offers: List<ShoppingCartOffer>,
) {
    val totalInPence: Int by lazy {
        items.sumOf { it.priceInPence } + offers.sumOf { it.priceInPence }
    }
}
