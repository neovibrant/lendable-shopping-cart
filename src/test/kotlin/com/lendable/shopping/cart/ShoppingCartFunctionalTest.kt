package com.lendable.shopping.cart

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class ShoppingCartFunctionalTest {
    private val printer = TestPrinter()
    private val catalogue = Catalogue()
    private val cart = ShoppingCart(catalogue, printer)

    @Test
    fun `checkout and print receipt for a single item`() {
        cart.add("CORNFLAKES", 1)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
              1 x Cornflakes               0.50 
            
                                          Total 
                                          £0.50 
            """.trimIndent(),
        )
    }

    @Test
    fun `checkout and print receipt for several items`() {
        cart.add("CORNFLAKES", 1)
            .add("BREAD", 1)
            .add("PASTA", 1)
            .add("TABASCO", 1)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
              1 x Cornflakes               0.50 
              1 x Bread                    1.00 
              1 x Pasta                    2.00 
              1 x Tabasco                  3.00 

                                          Total 
                                          £6.50 
            """.trimIndent(),
        )
    }

    @Test
    fun `checkout and print receipt for several items with several quantities`() {
        cart.add("CORNFLAKES", 1)
            .add("BREAD", 1)
            .add("PASTA", 2)
            .add("BREAD", 1)
            .add("TABASCO", 23)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
              1 x Cornflakes               0.50 
              2 x Bread                    2.00 
              2 x Pasta                    4.00 
             23 x Tabasco                 69.00 
            
                                          Total 
                                         £75.50 
            """.trimIndent(),
        )
    }

    @Test
    fun `checkout and print receipt for several items, quantities and the cornflakes offer`() {
        cart.add("CORNFLAKES", 2)
            .add("BREAD", 1)
            .add("PASTA", 2)
            .add("BREAD", 1)
            .add("TABASCO", 23)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
              2 x Cornflakes               1.00 
              2 x Bread                    2.00 
              2 x Pasta                    4.00 
             23 x Tabasco                 69.00 
            
            Offers
            
            Qty | Offer name           |      £ 
            ----+----------------------+--------
              1 x Cornflakes 2 for 1      -0.50 
            
                                          Total 
                                         £75.50 
            """.trimIndent(),
        )
    }

    @Test
    fun `checkout and print receipt for several items, quantities and offers`() {
        cart.add("CORNFLAKES", 2)
            .add("BREAD", 1)
            .add("PASTA", 2)
            .add("CORNFLAKES", 1)
            .add("BREAD", 1)
            .add("TABASCO", 23)
            .add("CORNFLAKES", 10)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
             13 x Cornflakes               6.50 
              2 x Bread                    2.00 
              2 x Pasta                    4.00 
             23 x Tabasco                 69.00 
            
            Offers
            
            Qty | Offer name           |      £ 
            ----+----------------------+--------
              6 x Cornflakes 2 for 1      -3.00 
            
                                          Total 
                                         £78.50 
            """.trimIndent(),
        )
    }

    @Test
    fun `checkout and print receipt for several items, quantities and offers after removing items`() {
        cart.add("CORNFLAKES", 2)
            .add("BREAD", 1)
            .add("PASTA", 2)
            .add("CORNFLAKES", 1)
            .add("BREAD", 1)
            .add("TABASCO", 23)
            .add("CORNFLAKES", 10)
            .remove("BREAD", 2)
            .remove("CORNFLAKES", 11)
            .checkout()

        assertThat(printer.getOutput()).isEqualTo(
            """
            Receipt
            
            Qty | Item name            |      £ 
            ----+----------------------+--------
              2 x Cornflakes               1.00 
              2 x Pasta                    4.00 
             23 x Tabasco                 69.00 
            
            Offers
            
            Qty | Offer name           |      £ 
            ----+----------------------+--------
              1 x Cornflakes 2 for 1      -0.50 
            
                                          Total 
                                         £73.50 
            """.trimIndent(),
        )
    }
}
