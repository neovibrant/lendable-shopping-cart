package com.lendable.shopping.cart

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.lendable.shopping.cart.exceptions.InvalidQuantityException
import com.lendable.shopping.cart.exceptions.ItemNotInCartException
import com.lendable.shopping.cart.exceptions.ProductNotFoundException
import com.lendable.shopping.cart.exceptions.QuantityToRemoveTooLargeException
import com.lendable.shopping.cart.exceptions.ShoppingCartIsEmptyException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ShoppingCartTest {
    private val catalogue = mockk<Catalogue>()
    private val printer = mockk<Printer>(relaxed = true)
    private val cart = ShoppingCart(catalogue, printer)

    @Test
    fun `should add an item to the cart`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        val receipt = cart.add(productCode, 1).checkout()

        assertThat(receipt.items).isEqualTo(
            listOf(ShoppingCartItem(Product(productCode, name, priceInPence), 1)),
        )
    }

    @Test
    fun `should add an item to the cart several times`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        val receipt =
            cart.add(productCode, 1)
                .add(productCode, 2)
                .add(productCode, 3)
                .checkout()

        assertThat(receipt.items).isEqualTo(
            listOf(ShoppingCartItem(Product(productCode, name, priceInPence), 6)),
        )
    }

    @Test
    fun `should add two different items to the cart`() {
        val productCode1 = "CORNFLAKES"
        val name1 = "Cornflakes"
        val priceInPence1 = 275

        val productCode2 = "HONEY"
        val name2 = "Honey"
        val priceInPence2 = 499

        every { catalogue.findProductByCode(productCode1) } answers {
            Product(productCode1, name1, priceInPence1)
        }
        every { catalogue.findProductByCode(productCode2) } answers {
            Product(productCode2, name2, priceInPence2)
        }
        every { catalogue.offers } returns emptyList()

        val receipt =
            cart
                .add(productCode1, 1)
                .add(productCode2, 2)
                .add(productCode1, 3)
                .add(productCode2, 4)
                .checkout()

        assertThat(receipt.items).isEqualTo(
            listOf(
                ShoppingCartItem(Product(productCode1, name1, priceInPence1), 4),
                ShoppingCartItem(Product(productCode2, name2, priceInPence2), 6),
            ),
        )
    }

    @Test
    fun `should generate offer when adding 2-for-1 items to the cart 2 times`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275
        val offerName = "Cornflakes 2 for 1"

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
            )

        val receipt =
            cart
                .add(productCode, 1)
                .add(productCode, 1)
                .checkout()

        assertThat(receipt.offers).isEqualTo(
            listOf(
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
                    -priceInPence,
                    1,
                ),
            ),
        )
    }

    @Test
    fun `should generate 2 offers when adding 2-for-1 items to the cart 4 times`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275
        val offerName = "Cornflakes 2 for 1"

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
            )

        val receipt =
            cart
                .add(productCode, 1)
                .add(productCode, 1)
                .add(productCode, 1)
                .add(productCode, 1)
                .checkout()

        assertThat(receipt.offers).isEqualTo(
            listOf(
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
                    -priceInPence,
                    2,
                ),
            ),
        )
    }

    @Test
    fun `should generate 2 offers when adding 2-for-1 items to the cart 5 times`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275
        val offerName = "Cornflakes 2 for 1"

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
            )

        val receipt =
            cart
                .add(productCode, 1)
                .add(productCode, 1)
                .add(productCode, 1)
                .add(productCode, 1)
                .add(productCode, 1)
                .checkout()

        assertThat(receipt.offers).isEqualTo(
            listOf(
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
                    -priceInPence,
                    2,
                ),
            ),
        )
    }

    @Test
    fun `should generate 2 separate offers when adding separate 2-for-1 items to the cart 2 times each`() {
        val productCode1 = "CORNFLAKES"
        val name1 = "Cornflakes"
        val priceInPence1 = 275
        val offerName1 = "Cornflakes 2 for 1"

        val productCode2 = "TABASCO"
        val name2 = "Tabasco"
        val priceInPence2 = 999
        val offerName2 = "Tabasco 2 for 1"

        every { catalogue.findProductByCode(productCode1) } answers {
            Product(productCode1, name1, priceInPence1)
        }
        every { catalogue.findProductByCode(productCode2) } answers {
            Product(productCode2, name2, priceInPence2)
        }
        every { catalogue.offers } returns
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode1, offerName1),
                Offer(OfferType.TWO_FOR_ONE, productCode2, offerName2),
            )

        val receipt =
            cart
                .add(productCode1, 1)
                .add(productCode2, 2)
                .add(productCode1, 1)
                .checkout()

        assertThat(receipt.offers).isEqualTo(
            listOf(
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode1, offerName1),
                    -priceInPence1,
                    1,
                ),
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode2, offerName2),
                    -priceInPence2,
                    1,
                ),
            ),
        )
    }

    @Test
    fun `should generate receipt at checkout and print it`() {
        val productCode1 = "CORNFLAKES"
        val name1 = "Cornflakes"
        val priceInPence1 = 275
        val offerName1 = "Cornflakes 2 for 1"

        val productCode2 = "TABASCO"
        val name2 = "Tabasco"
        val priceInPence2 = 999

        every { catalogue.findProductByCode(productCode1) } answers {
            Product(productCode1, name1, priceInPence1)
        }
        every { catalogue.findProductByCode(productCode2) } answers {
            Product(productCode2, name2, priceInPence2)
        }
        every { catalogue.offers } returns
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode1, offerName1),
            )

        val receipt =
            cart
                .add(productCode1, 1)
                .add(productCode2, 2)
                .add(productCode1, 3)
                .add(productCode2, 4)
                .checkout()

        verify { printer.print(receipt) }
    }

    @Test
    fun `should fail to add non-existent product to cart`() {
        val nonExistentProductCode = "no-such-thing"

        every { catalogue.findProductByCode(nonExistentProductCode) } returns null
        every { catalogue.offers } returns emptyList()

        assertThrows<ProductNotFoundException> {
            cart.add(nonExistentProductCode, 1)
        }
    }

    @Test
    fun `should fail to add item with negative quantity`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        assertThrows<InvalidQuantityException> {
            cart.add(productCode, -1)
        }
    }

    @Test
    fun `should fail to add item with quantity greater than 99`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        assertThrows<InvalidQuantityException> {
            cart.add(productCode, 100)
        }
    }

    @Test
    fun `should fail to add item repeatedly if quantity would become greater that 99`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        val cart = cart.add(productCode, 99)
        assertThrows<InvalidQuantityException> {
            cart.add(productCode, 1)
        }
    }

    @Test
    fun `should remove an item from the cart`() {
        val productCode1 = "CORNFLAKES"
        val name1 = "Cornflakes"
        val priceInPence1 = 275

        val productCode2 = "HONEY"
        val name2 = "Honey"
        val priceInPence2 = 499

        every { catalogue.findProductByCode(productCode1) } answers {
            Product(productCode1, name1, priceInPence1)
        }
        every { catalogue.findProductByCode(productCode2) } answers {
            Product(productCode2, name2, priceInPence2)
        }
        every { catalogue.offers } returns emptyList()

        val receipt =
            cart
                .add(productCode1, 1)
                .add(productCode2, 1)
                .remove(productCode1, 1)
                .checkout()

        assertThat(receipt.items).isEqualTo(
            listOf(
                ShoppingCartItem(Product(productCode2, name2, priceInPence2), 1),
            ),
        )
    }

    @Test
    fun `should decrease quantity of an item from the cart`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        val receipt =
            cart
                .add(productCode, 2)
                .remove(productCode, 1)
                .checkout()

        assertThat(receipt.items).isEqualTo(
            listOf(ShoppingCartItem(Product(productCode, name, priceInPence), 1)),
        )
    }

    @Test
    fun `should invalidate offer when removing an item from the 2-for-1 of 2 items`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275
        val offerName = "Cornflakes 2 for 1"

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } answers {
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
            )
        }

        val receipt =
            cart
                .add(productCode, 2)
                .remove(productCode, 1)
                .checkout()

        assertThat(receipt.offers).isEmpty()
    }

    @Test
    fun `should decrease offer when removing an item from the 2-for-1 of 4 items`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275
        val offerName = "Cornflakes 2 for 1"

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } answers {
            listOf(
                Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
            )
        }

        val receipt =
            cart
                .add(productCode, 4)
                .remove(productCode, 1)
                .checkout()

        assertThat(receipt.offers).isEqualTo(
            listOf(
                ShoppingCartOffer(
                    Offer(OfferType.TWO_FOR_ONE, productCode, offerName),
                    -priceInPence,
                    1,
                ),
            ),
        )
    }

    @Test
    fun `should fail to remove non-existent product from cart`() {
        val nonExistentProductCode = "no-such-thing"

        every { catalogue.findProductByCode(nonExistentProductCode) } returns null
        every { catalogue.offers } returns emptyList()

        assertThrows<ItemNotInCartException> {
            cart.remove(nonExistentProductCode, 1)
        }
    }

    @Test
    fun `should fail to remove item with negative quantity`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        assertThrows<InvalidQuantityException> {
            cart.remove(productCode, -1)
        }
    }

    @Test
    fun `should fail to remove item with quantity greater than 99`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        assertThrows<InvalidQuantityException> {
            cart.remove(productCode, 100)
        }
    }

    @Test
    fun `should fail to remove item when not enough quantity is present in basket`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        val cart = cart.add(productCode, 11).remove(productCode, 1)
        assertThrows<QuantityToRemoveTooLargeException> {
            cart.remove(productCode, 11)
        }
    }

    @Test
    fun `cannot checkout an empty shopping cart (nothing was added)`() {
        assertThrows<ShoppingCartIsEmptyException> {
            cart.checkout()
        }
    }

    @Test
    fun `cannot checkout an empty shopping cart (everything was removed)`() {
        val productCode = "CORNFLAKES"
        val name = "Cornflakes"
        val priceInPence = 275

        every { catalogue.findProductByCode(productCode) } answers {
            Product(productCode, name, priceInPence)
        }
        every { catalogue.offers } returns emptyList()

        assertThrows<ShoppingCartIsEmptyException> {
            cart
                .add(productCode, 1)
                .remove(productCode, 1)
                .checkout()
        }
    }
}
