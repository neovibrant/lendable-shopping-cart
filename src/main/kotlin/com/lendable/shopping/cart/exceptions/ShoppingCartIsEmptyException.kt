package com.lendable.shopping.cart.exceptions

import java.lang.IllegalStateException

class ShoppingCartIsEmptyException : IllegalStateException(
    "Cannot checkout an empty cart",
)
