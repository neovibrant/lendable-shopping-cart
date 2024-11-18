package com.lendable.shopping.cart.offers

import com.lendable.shopping.cart.Offer
import com.lendable.shopping.cart.OfferType.TWO_FOR_ONE

class OfferRuleFactory {
    fun create(offer: Offer): OfferRule {
        return when (offer.type) {
            TWO_FOR_ONE -> TwoForOneOfferRule()
        }
    }
}
