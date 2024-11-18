package com.lendable.shopping.cart.exceptions

class QuantityToRemoveTooLargeException(quantity: Int) : IllegalArgumentException(
    "Quantity of $quantity is too large, not enough items in cart",
)
