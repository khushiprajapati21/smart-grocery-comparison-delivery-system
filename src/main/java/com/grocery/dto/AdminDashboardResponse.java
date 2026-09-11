package com.grocery.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalCustomers;
    private long totalShopOwners;
    private long totalDeliveryAgents;

    private long totalShops;
    private long activeShops;

    private long totalCategories;

    private long totalProducts;
    private long availableProducts;

    private long totalOrders;

    public AdminDashboardResponse() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalShopOwners() {
        return totalShopOwners;
    }

    public void setTotalShopOwners(long totalShopOwners) {
        this.totalShopOwners = totalShopOwners;
    }

    public long getTotalDeliveryAgents() {
        return totalDeliveryAgents;
    }

    public void setTotalDeliveryAgents(long totalDeliveryAgents) {
        this.totalDeliveryAgents = totalDeliveryAgents;
    }

    public long getTotalShops() {
        return totalShops;
    }

    public void setTotalShops(long totalShops) {
        this.totalShops = totalShops;
    }

    public long getActiveShops() {
        return activeShops;
    }

    public void setActiveShops(long activeShops) {
        this.activeShops = activeShops;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getAvailableProducts() {
        return availableProducts;
    }

    public void setAvailableProducts(long availableProducts) {
        this.availableProducts = availableProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }
}