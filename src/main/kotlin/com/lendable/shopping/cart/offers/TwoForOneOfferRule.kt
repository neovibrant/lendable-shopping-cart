package com.lendable.shopping.cart.offers

import com.lendable.shopping.cart.Offer
import com.lendable.shopping.cart.ShoppingCartItem
import com.lendable.shopping.cart.ShoppingCartOffer

class TwoForOneOfferRule : OfferRule {
    override fun apply(
        offer: Offer,
        items: List<ShoppingCartItem>,
    ): ShoppingCartOffer? {
        val productsOnOffer = items.filter { it.product.code == offer.productCode }

        if (productsOnOffer.isEmpty()) {
            return null
        }

        val quantityOfItemsOnOffer = productsOnOffer.sumOf { it.quantity }

        if (quantityOfItemsOnOffer < 2) {
            return null
        }

        val quantityOfOffers = quantityOfItemsOnOffer / 2
        val valueInPence = productsOnOffer.first().product.priceInPence * -1

        return ShoppingCartOffer(offer, valueInPence, quantityOfOffers)
    }
}
