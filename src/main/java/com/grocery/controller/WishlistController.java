package com.grocery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.AddToWishlistRequest;
import com.grocery.dto.WishlistResponse;
import com.grocery.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Add Product To Wishlist
     */
    @PostMapping("/add")
    public ResponseEntity<WishlistResponse> addToWishlist(
            @RequestBody AddToWishlistRequest request) {

        WishlistResponse response =
                wishlistService.addToWishlist(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get Wishlist
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<WishlistResponse> getWishlist(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                wishlistService.getWishlist(customerId));
    }

    /**
     * Remove Product From Wishlist
     */
    @DeleteMapping("/remove/{customerId}/{productId}")
    public ResponseEntity<String> removeProduct(
            @PathVariable Long customerId,
            @PathVariable Long productId) {

        wishlistService.removeProduct(customerId, productId);

        return ResponseEntity.ok(
                "Product removed from wishlist successfully.");
    }

    /**
     * Clear Wishlist
     */
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<String> clearWishlist(
            @PathVariable Long customerId) {

        wishlistService.clearWishlist(customerId);

        return ResponseEntity.ok(
                "Wishlist cleared successfully.");
    }
}
