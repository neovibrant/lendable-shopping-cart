package com.lendable.shopping.cart

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CatalogueTest {
    private val catalogue = Catalogue()

    @Test
    fun `should get cornflakes by code`() {
        val product = catalogue.findProductByCode("CORNFLAKES")
        assertThat(product).isEqualTo(Product("CORNFLAKES", "Cornflakes", 50))
    }

    @Test
    fun `should get something other than cornflakes by code`() {
        val product = catalogue.findProductByCode("BREAD")
        assertThat(product).isEqualTo(Product("BREAD", "Bread", 100))
    }

    @Test
    fun `should get offers from catalogue`() {
        assertThat(catalogue.offers).contains(
            Offer(OfferType.TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1"),
        )
    }
}
