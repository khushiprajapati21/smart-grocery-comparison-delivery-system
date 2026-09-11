package com.grocery.service;

import com.grocery.dto.AddToCartRequest;
import com.grocery.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request);

    CartResponse getCart(Long customerId);

    CartResponse updateQuantity(Long customerId,
                                Long productId,
                                Integer quantity);

    void removeProduct(Long customerId,
                       Long productId);

    void clearCart(Long customerId);

}
