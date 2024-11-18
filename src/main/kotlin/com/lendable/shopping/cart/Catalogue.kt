package com.lendable.shopping.cart

class Catalogue {
    private val products =
        listOf(
            Product("CORNFLAKES", "Cornflakes", 50),
            Product("BREAD", "Bread", 100),
            Product("PASTA", "Pasta", 200),
            Product("TABASCO", "Tabasco", 300),
        )
    val offers =
        listOf(
            Offer(OfferType.TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1"),
        )

    fun findProductByCode(code: ProductCode): Product? {
        return products.find { it.code == code }
    }
}
