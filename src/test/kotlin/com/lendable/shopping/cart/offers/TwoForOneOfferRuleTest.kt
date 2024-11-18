package com.lendable.shopping.cart.offers

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.lendable.shopping.cart.Offer
import com.lendable.shopping.cart.OfferType.TWO_FOR_ONE
import com.lendable.shopping.cart.Product
import com.lendable.shopping.cart.ShoppingCartItem
import com.lendable.shopping.cart.ShoppingCartOffer
import org.junit.jupiter.api.Test

class TwoForOneOfferRuleTest {
    private val rule = TwoForOneOfferRule()

    @Test
    fun `apply two for one offer rule once`() {
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("CORNFLAKES", "Cornflakes", 50), 2),
                ShoppingCartItem(Product("BREAD", "Bread", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isEqualTo(ShoppingCartOffer(offer, -50, 1))
    }

    @Test
    fun `apply two for one offer rule twice`() {
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("CORNFLAKES", "Cornflakes", 50), 4),
                ShoppingCartItem(Product("SOMETHING_ELSE", "Something else", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isEqualTo(ShoppingCartOffer(offer, -50, 2))
    }

    @Test
    fun `apply two for one offer rule twice with remainder`() {
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("CORNFLAKES", "Cornflakes", 50), 5),
                ShoppingCartItem(Product("SOMETHING_ELSE", "Something else", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isEqualTo(ShoppingCartOffer(offer, -50, 2))
    }

    @Test
    fun `apply two for one offer not limited to cornflakes`() {
        val offer = Offer(TWO_FOR_ONE, "GRANOLA", "Granola 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("GRANOLA", "Granola", 750), 2),
                ShoppingCartItem(Product("SOMETHING_ELSE", "Something else", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isEqualTo(ShoppingCartOffer(offer, -750, 1))
    }

    @Test
    fun `no offer to apply when only 1 item`() {
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("CORNFLAKES", "Cornflakes", 50), 1),
                ShoppingCartItem(Product("SOMETHING_ELSE", "Something else", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isNull()
    }

    @Test
    fun `no offer to apply when item missing altogether`() {
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val items =
            listOf(
                ShoppingCartItem(Product("SOMETHING_ELSE", "Something else", 100), 1),
            )

        val result = rule.apply(offer, items)

        assertThat(result).isNull()
    }
}
