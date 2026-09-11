package com.grocery.service;

import com.grocery.dto.AddToWishlistRequest;
import com.grocery.dto.WishlistResponse;

public interface WishlistService {

    WishlistResponse addToWishlist(AddToWishlistRequest request);

    WishlistResponse getWishlist(Long customerId);

    void removeProduct(Long customerId, Long productId);

    void clearWishlist(Long customerId);

}
