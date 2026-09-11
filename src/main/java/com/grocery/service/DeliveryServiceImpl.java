package com.grocery.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.AssignDeliveryRequest;
import com.grocery.dto.DeliveryResponse;
import com.grocery.dto.UpdateDeliveryStatusRequest;
import com.grocery.entity.Delivery;
import com.grocery.entity.DeliveryStatus;
import com.grocery.entity.Order;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.DeliveryRepository;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.UserRepository;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository,
                               OrderRepository orderRepository,
                               UserRepository userRepository) {

        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Assign Delivery Agent
     */
    @Override
    public DeliveryResponse assignDelivery(AssignDeliveryRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        User deliveryAgent = userRepository.findById(request.getDeliveryAgentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery agent not found."));

        if (deliveryAgent.getRole() != Role.DELIVERY_AGENT) {
            throw new BadRequestException("Selected user is not a delivery agent.");
        }

        if (deliveryRepository.existsByOrder(order)) {
            throw new DuplicateResourceException("Delivery already assigned for this order.");
        }

        Delivery delivery = new Delivery();

        delivery.setOrder(order);
        delivery.setDeliveryAgent(deliveryAgent);
        delivery.setStatus(DeliveryStatus.ASSIGNED);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(savedDelivery);
    }

    /**
     * Update Delivery Status
     */
    @Override
    public DeliveryResponse updateDeliveryStatus(Long deliveryId,
                                                 UpdateDeliveryStatusRequest request) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery not found."));

        DeliveryStatus status;

        try {
            status = DeliveryStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid delivery status.");
        }

        delivery.setStatus(status);

        if (status == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return mapToResponse(updatedDelivery);
    }

    /**
     * Get Delivery By Id
     */
    @Override
    public DeliveryResponse getDeliveryById(Long deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery not found."));

        return mapToResponse(delivery);
    }

    /**
     * Get Deliveries By Delivery Agent
     */
    @Override
    public List<DeliveryResponse> getDeliveriesByDeliveryAgent(Long deliveryAgentId) {

        User deliveryAgent = userRepository.findById(deliveryAgentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Delivery agent not found."));

        if (deliveryAgent.getRole() != Role.DELIVERY_AGENT) {
            throw new BadRequestException("User is not a delivery agent.");
        }

        return deliveryRepository.findByDeliveryAgent(deliveryAgent)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> DTO
     */
    private DeliveryResponse mapToResponse(Delivery delivery) {

        DeliveryResponse response = new DeliveryResponse();

        response.setDeliveryId(delivery.getId());

        response.setOrderId(delivery.getOrder().getId());

        response.setDeliveryAgentId(
                delivery.getDeliveryAgent().getId());

        response.setDeliveryAgentName(
                delivery.getDeliveryAgent().getName());

        response.setStatus(
                delivery.getStatus().name());

        response.setAssignedAt(
                delivery.getAssignedAt());

        response.setDeliveredAt(
                delivery.getDeliveredAt());

        return response;
    }
}