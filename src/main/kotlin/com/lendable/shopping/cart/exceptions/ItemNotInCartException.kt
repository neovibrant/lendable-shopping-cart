package com.lendable.shopping.cart.exceptions

import com.lendable.shopping.cart.ProductCode

class ItemNotInCartException(productCode: ProductCode) : IllegalArgumentException("Item not found in cart: $productCode")
