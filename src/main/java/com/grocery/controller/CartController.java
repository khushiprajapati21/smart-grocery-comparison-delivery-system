package com.grocery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.AddToCartRequest;
import com.grocery.dto.CartResponse;
import com.grocery.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add Product to Cart
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody AddToCartRequest request) {

        CartResponse response = cartService.addToCart(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * View Customer Cart
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                cartService.getCart(customerId));
    }

    /**
     * Update Product Quantity
     */
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(

            @RequestParam Long customerId,

            @RequestParam Long productId,

            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        customerId,
                        productId,
                        quantity));
    }

    /**
     * Remove Product From Cart
     */
    @DeleteMapping("/remove/{customerId}/{productId}")
    public ResponseEntity<String> removeProduct(

            @PathVariable Long customerId,

            @PathVariable Long productId) {

        cartService.removeProduct(customerId, productId);

        return ResponseEntity.ok("Product removed from cart successfully.");
    }

    /**
     * Clear Cart
     */
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<String> clearCart(
            @PathVariable Long customerId) {

        cartService.clearCart(customerId);

        return ResponseEntity.ok("Cart cleared successfully.");
    }

}
