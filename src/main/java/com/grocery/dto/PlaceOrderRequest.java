package com.grocery.dto;

public class PlaceOrderRequest {

    private Long customerId;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
