package com.grocery.dto;

import java.time.LocalDateTime;

public class DeliveryResponse {

    private Long deliveryId;

    private Long orderId;

    private Long deliveryAgentId;

    private String deliveryAgentName;

    private String status;

    private LocalDateTime assignedAt;

    private LocalDateTime deliveredAt;

    public DeliveryResponse() {
    }

    public DeliveryResponse(Long deliveryId,
                            Long orderId,
                            Long deliveryAgentId,
                            String deliveryAgentName,
                            String status,
                            LocalDateTime assignedAt,
                            LocalDateTime deliveredAt) {

        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.deliveryAgentId = deliveryAgentId;
        this.deliveryAgentName = deliveryAgentName;
        this.status = status;
        this.assignedAt = assignedAt;
        this.deliveredAt = deliveredAt;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
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

    public void setDeliveryAgentId(Long deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    public String getDeliveryAgentName() {
        return deliveryAgentName;
    }

    public void setDeliveryAgentName(String deliveryAgentName) {
        this.deliveryAgentName = deliveryAgentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}
