package com.lendable.shopping.cart.exceptions

class InvalidQuantityException(quantity: Int) : IllegalArgumentException(
    "Quantity of $quantity is not valid, it must be between 1 and 99 (inclusive)",
)
