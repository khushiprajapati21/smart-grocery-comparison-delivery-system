package com.grocery.service;

import java.util.List;

import com.grocery.dto.AssignDeliveryRequest;
import com.grocery.dto.DeliveryResponse;
import com.grocery.dto.UpdateDeliveryStatusRequest;

public interface DeliveryService {

    DeliveryResponse assignDelivery(AssignDeliveryRequest request);

    DeliveryResponse updateDeliveryStatus(Long deliveryId,
                                          UpdateDeliveryStatusRequest request);

    DeliveryResponse getDeliveryById(Long deliveryId);

    List<DeliveryResponse> getDeliveriesByDeliveryAgent(Long deliveryAgentId);
}
