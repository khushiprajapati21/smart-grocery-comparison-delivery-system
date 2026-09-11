package com.grocery.dto;

import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;

    private String productName;

    private String description;

    private Double price;

    private Integer stockQuantity;

    private String unit;

    private Boolean available;

    private Boolean active;

    private LocalDateTime createdAt;

    private Long shopId;

    private String shopName;

    private Long categoryId;

    private String categoryName;

    public ProductResponse() {
    }

    public ProductResponse(Long id,
                           String productName,
                           String description,
                           Double price,
                           Integer stockQuantity,
                           String unit,
                           Boolean available,
                           Boolean active,
                           LocalDateTime createdAt,
                           Long shopId,
                           String shopName,
                           Long categoryId,
                           String categoryName) {

        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.unit = unit;
        this.available = available;
        this.active = active;
        this.createdAt = createdAt;
        this.shopId = shopId;
        this.shopName = shopName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
