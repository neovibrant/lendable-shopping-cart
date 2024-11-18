package com.lendable.shopping.cart

import java.text.NumberFormat
import java.text.NumberFormat.getNumberInstance
import java.util.Locale.UK

class TestPrinter : Printer {
    private var output: String = ""

    override fun print(receipt: Receipt) {
        output = ""
        output += "Receipt\n"
        output += "\n"
        output += "Qty | Item name            |      £ \n"
        output += "----+----------------------+--------\n"
        receipt.items.forEach {
            output += " ${padQuantity(it.quantity)} x ${padName(it.product.name)}   ${formatAndPadPrice(it.priceInPence)} \n"
        }

        if (receipt.offers.isNotEmpty()) {
            output += "\n"
            output += "Offers\n"
            output += "\n"
            output += "Qty | Offer name           |      £ \n"
            output += "----+----------------------+--------\n"
            receipt.offers.forEach {
                output += " ${padQuantity(it.quantity)} x ${padName(it.offer.name)}   ${
                    formatAndPadPrice(
                        it.priceInPence,
                    )
                } \n"
            }
        }

        output += "\n"
        output += "                              Total \n"
        output += " ${formatAndPadTotalPrice(receipt.totalInPence)} "
    }

    private fun padName(value: String): String {
        val size = 20
        if (value.length > size) {
            return value.substring(0, size - 1) + "…"
        }
        return value.padEnd(size)
    }

    private fun padQuantity(quantity: Int): String {
        if (quantity > 99) {
            throw IllegalArgumentException("Quantities greater than 99 are not supported by this test printer")
        }
        return getNumberInstance(UK).format(quantity).padStart(2)
    }

    private fun formatAndPadPrice(priceInPence: Int): String {
        val price = String.format(UK, "%.2f", priceInPence / 100.0)
        return price.padStart(6)
    }

    private fun formatAndPadTotalPrice(totalPriceInPence: Int): String {
        return NumberFormat
            .getCurrencyInstance(UK).format(totalPriceInPence / 100.0).padStart(34)
    }

    fun getOutput(): String {
        return output
    }
}
