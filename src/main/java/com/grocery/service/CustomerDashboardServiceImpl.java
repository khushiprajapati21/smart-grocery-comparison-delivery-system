package com.grocery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grocery.dto.CustomerDashboardResponse;
import com.grocery.dto.RecentOrderResponse;
import com.grocery.entity.Order;
import com.grocery.entity.OrderStatus;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.UserRepository;

@Service
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CustomerDashboardServiceImpl(UserRepository userRepository,
                                        OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public CustomerDashboardResponse getDashboard(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        CustomerDashboardResponse response =
                new CustomerDashboardResponse();

        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getName());

        response.setTotalOrders(
                orderRepository.countByCustomer(customer));

        response.setPendingOrders(
                orderRepository.countByCustomerAndStatus(
                        customer,
                        OrderStatus.PENDING));

        response.setOutForDeliveryOrders(
                orderRepository.countByCustomerAndStatus(
                        customer,
                        OrderStatus.OUT_FOR_DELIVERY));

        response.setDeliveredOrders(
                orderRepository.countByCustomerAndStatus(
                        customer,
                        OrderStatus.DELIVERED));

        response.setCancelledOrders(
                orderRepository.countByCustomerAndStatus(
                        customer,
                        OrderStatus.CANCELLED));

        Double spent = orderRepository.getTotalSpent(customer);

        response.setTotalSpent(spent == null ? 0.0 : spent);

        List<Order> recentOrders =
                orderRepository.findTop5ByCustomerOrderByOrderDateDesc(customer);

        List<RecentOrderResponse> recentOrderResponses =
                new ArrayList<>();

        for (Order order : recentOrders) {

            RecentOrderResponse dto = new RecentOrderResponse();

            dto.setOrderId(order.getId());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus().name());
            dto.setOrderDate(order.getOrderDate());

            recentOrderResponses.add(dto);
        }

        response.setRecentOrders(recentOrderResponses);

        return response;
    }
}
