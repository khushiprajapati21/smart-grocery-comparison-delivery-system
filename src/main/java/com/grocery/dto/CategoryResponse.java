package com.grocery.dto;

import java.time.LocalDateTime;

public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String description;

    private Long shopId;
    private String shopName;

    private boolean active;
    private LocalDateTime createdAt;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id,
                            String categoryName,
                            String description,
                            Long shopId,
                            String shopName,
                            boolean active,
                            LocalDateTime createdAt) {

        this.id = id;
        this.categoryName = categoryName;
        this.description = description;
        this.shopId = shopId;
        this.shopName = shopName;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
