package com.grocery.service;

import java.util.List;

import com.grocery.dto.OrderResponse;
import com.grocery.dto.PlaceOrderRequest;
import com.grocery.dto.UpdateOrderStatusRequest;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    List<OrderResponse> getCustomerOrders(Long customerId);
    
    OrderResponse updateOrderStatus(Long orderId,
            UpdateOrderStatusRequest request);

    List<OrderResponse> getAllOrders();
}
