package com.lendable.shopping.cart

data class Offer(
    val type: OfferType,
    val productCode: ProductCode,
    val name: String,
)

enum class OfferType {
    TWO_FOR_ONE,
}
