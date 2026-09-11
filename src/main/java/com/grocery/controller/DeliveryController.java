package com.grocery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.AssignDeliveryRequest;
import com.grocery.dto.DeliveryResponse;
import com.grocery.dto.UpdateDeliveryStatusRequest;
import com.grocery.service.DeliveryService;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * Assign Delivery Boy
     */
    @PostMapping
    public ResponseEntity<DeliveryResponse> assignDelivery(
            @RequestBody AssignDeliveryRequest request) {

        DeliveryResponse response =
                deliveryService.assignDelivery(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update Delivery Status
     */
    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(

            @PathVariable Long deliveryId,

            @RequestBody UpdateDeliveryStatusRequest request) {

        return ResponseEntity.ok(
                deliveryService.updateDeliveryStatus(
                        deliveryId,
                        request));
    }

    /**
     * Get Delivery By Id
     */
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> getDelivery(

            @PathVariable Long deliveryId) {

        return ResponseEntity.ok(
                deliveryService.getDeliveryById(deliveryId));
    }

    /**
     * Get Deliveries of Delivery Boy
     */
    @GetMapping("/delivery-agent/{deliveryagentId}")
    public ResponseEntity<List<DeliveryResponse>> getDeliveryAgentOrders(
            @PathVariable Long deliveryAgentId) {

        return ResponseEntity.ok(
                deliveryService.getDeliveriesByDeliveryAgent(deliveryAgentId));
    }

}
