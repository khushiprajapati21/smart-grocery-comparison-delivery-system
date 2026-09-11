package com.grocery.dto;

public class ShopOwnerDashboardResponse {

    private Long shopId;
    private String shopName;

    private Long totalCategories;
    private Long totalProducts;
    private Long activeProducts;
    private Long outOfStockProducts;

    private Long totalOrders;
    private Long pendingOrders;
    private Long preparingOrders;
    private Long readyForPickupOrders;
    private Long deliveredOrders;

    private Double totalRevenue;

    public ShopOwnerDashboardResponse() {
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

    public Long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(Long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getActiveProducts() {
        return activeProducts;
    }

    public void setActiveProducts(Long activeProducts) {
        this.activeProducts = activeProducts;
    }

    public Long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(Long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Long getPreparingOrders() {
        return preparingOrders;
    }

    public void setPreparingOrders(Long preparingOrders) {
        this.preparingOrders = preparingOrders;
    }

    public Long getReadyForPickupOrders() {
        return readyForPickupOrders;
    }

    public void setReadyForPickupOrders(Long readyForPickupOrders) {
        this.readyForPickupOrders = readyForPickupOrders;
    }

    public Long getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(Long deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
