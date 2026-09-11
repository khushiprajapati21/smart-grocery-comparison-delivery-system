package com.grocery.dto;

public class UpdateDeliveryStatusRequest {

    private String status;

    public UpdateDeliveryStatusRequest() {
    }

    public UpdateDeliveryStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
