package com.grocery.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WishlistResponse {

    private Long wishlistId;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;

    private List<WishlistItemResponse> items = new ArrayList<>();

    public WishlistResponse() {
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<WishlistItemResponse> getItems() {
        return items;
    }

    public void setItems(List<WishlistItemResponse> items) {
        this.items = items;
    }
}
