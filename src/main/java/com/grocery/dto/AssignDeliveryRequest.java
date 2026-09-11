package com.grocery.dto;

public class AssignDeliveryRequest {

    private Long orderId;
    private Long deliveryAgentId;

    public AssignDeliveryRequest() {
    }

    public AssignDeliveryRequest(Long orderId, Long deliveryBoyId) {
        this.orderId = orderId;
        this.deliveryAgentId = deliveryBoyId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public void setDeliveryAgentId(Long deliveryBoyId) {
        this.deliveryAgentId = deliveryBoyId;
    }
}
