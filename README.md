# Lendable Shopping Cart

This is a simple shopping cart application that allows users to add and remove items from their cart.

There is support for 2-for-1 offers on cornflakes added to cart.

## How to run

There is no UI for this application, the only way to run it is via tests.

There is a more "visual" test called `ShoppingCartFunctionalTest` that makes is easier to visualise adding, removing items and applying offers as well as the generated receipt.

## Tests

Most tests are unit tests except for `ShoppingCartFunctionalTest` which is a functional test.

The code was written using BDD by creating the functional test first and then iteratively implementing the code to make the test pass.

Unit tests were added later once the happy path was already implemented and passing. Edge cases (such as validation) were covered by unit tests and done via TDD whenever practical.

## Assumptions

I tried to keep to the requirements as much as possible and not make assumptions or over-engineer the solution, however, there are a few assumptions I made: 

 - it's useful to specify the quantity of an item when adding it to the cart despite the fact the same item can be added multiple times
 - quantities are integers and there are no units of quantities (e.g. kilograms or litres). This can be evolved later on if deemed necessary 
 - I didn't consider performance or scalability as it wasn't a requirement and I wanted to steer clear of any premature optimisation.

## Distribution

Unfortunately, Gmail didn't allow me to attach a zip file so I decided to add the code in Github. You can download it as a zip file from the repo.
