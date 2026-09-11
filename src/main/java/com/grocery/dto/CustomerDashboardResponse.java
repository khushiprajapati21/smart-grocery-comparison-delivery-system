package com.grocery.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerDashboardResponse {

    private Long customerId;
    private String customerName;

    private Long totalOrders;
    private Long pendingOrders;
    private Long outForDeliveryOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;

    private Double totalSpent;

    private List<RecentOrderResponse> recentOrders = new ArrayList<>();

    public CustomerDashboardResponse() {
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

    public Long getOutForDeliveryOrders() {
        return outForDeliveryOrders;
    }

    public void setOutForDeliveryOrders(Long outForDeliveryOrders) {
        this.outForDeliveryOrders = outForDeliveryOrders;
    }

    public Long getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(Long deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public Long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(Long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public List<RecentOrderResponse> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<RecentOrderResponse> recentOrders) {
        this.recentOrders = recentOrders;
    }
}
