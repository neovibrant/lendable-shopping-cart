package com.lendable.shopping.cart.exceptions

import com.lendable.shopping.cart.ProductCode

class ProductNotFoundException(productCode: ProductCode) : IllegalArgumentException("Product not found: $productCode")
