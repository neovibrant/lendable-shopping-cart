package com.lendable.shopping.cart.offers

import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.lendable.shopping.cart.Offer
import com.lendable.shopping.cart.OfferType.TWO_FOR_ONE
import org.junit.jupiter.api.Test

class OfferRuleFactoryTest {
    @Test
    fun `create two for one offer rule`() {
        val factory = OfferRuleFactory()
        val offer = Offer(TWO_FOR_ONE, "CORNFLAKES", "Cornflakes 2 for 1")
        val rule = factory.create(offer)
        assertThat(rule).isInstanceOf(TwoForOneOfferRule::class)
    }
}
