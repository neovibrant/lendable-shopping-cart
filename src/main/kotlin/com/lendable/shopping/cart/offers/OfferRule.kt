package com.lendable.shopping.cart.offers

import com.lendable.shopping.cart.Offer
import com.lendable.shopping.cart.ShoppingCartItem
import com.lendable.shopping.cart.ShoppingCartOffer

interface OfferRule {
    fun apply(
        offer: Offer,
        items: List<ShoppingCartItem>,
    ): ShoppingCartOffer?
}
