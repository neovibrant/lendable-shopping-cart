package com.lendable.shopping.cart

import com.lendable.shopping.cart.exceptions.InvalidQuantityException
import com.lendable.shopping.cart.exceptions.ItemNotInCartException
import com.lendable.shopping.cart.exceptions.ProductNotFoundException
import com.lendable.shopping.cart.exceptions.QuantityToRemoveTooLargeException
import com.lendable.shopping.cart.exceptions.ShoppingCartIsEmptyException
import com.lendable.shopping.cart.offers.OfferRuleFactory

class ShoppingCart(private val catalogue: Catalogue, private val printer: Printer) {
    private val offerRuleFactory = OfferRuleFactory()
    private val items = mutableListOf<ShoppingCartItem>()

    fun add(
        productCode: ProductCode,
        quantity: Int,
    ): ShoppingCart {
        val product =
            catalogue.findProductByCode(productCode)
                ?: throw ProductNotFoundException(productCode)

        if (findItemInCart(productCode) != null) {
            updateItemQuantityBy(productCode, quantity)
        } else {
            val validQuantity = validateQuantity(quantity)
            val item = ShoppingCartItem(product, validQuantity)
            items.add(item)
        }
        return this
    }

    fun remove(
        productCode: ProductCode,
        quantity: Int,
    ): ShoppingCart {
        val quantityToRemove = validateQuantity(quantity)
        val item =
            findItemInCart(productCode)
                ?: throw ItemNotInCartException(productCode)
        when {
            (quantityToRemove > item.quantity) -> throw QuantityToRemoveTooLargeException(quantityToRemove)
            (quantityToRemove == item.quantity) -> items.remove(item)
            else -> updateItemQuantityBy(productCode, -quantityToRemove)
        }
        return this
    }

    private fun updateItemQuantityBy(
        productCode: ProductCode,
        increaseQuantityBy: Int,
    ) {
        items.replaceAll { item ->
            if (item.product.code == productCode) {
                val validQuantity = validateQuantity(item.quantity + increaseQuantityBy)
                item.copy(quantity = validQuantity)
            } else {
                item
            }
        }
    }

    private fun validateQuantity(quantity: Int): Int {
        if (quantity < 1 || quantity > 99) {
            throw InvalidQuantityException(quantity)
        }
        return quantity
    }

    private fun applyOffers(): List<ShoppingCartOffer> {
        return catalogue
            .offers
            .mapNotNull { offerRuleFactory.create(it).apply(it, items) }
    }

    private fun findItemInCart(productCode: ProductCode): ShoppingCartItem? {
        return items.find { it.product.code == productCode }
    }

    fun checkout(): Receipt {
        if (items.isEmpty()) {
            throw ShoppingCartIsEmptyException()
        }

        val receipt = Receipt(items, applyOffers())
        printer.print(receipt)
        return receipt
    }
}
