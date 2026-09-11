package com.grocery.service;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.OrderItemResponse;
import com.grocery.dto.OrderResponse;
import com.grocery.dto.PlaceOrderRequest;
import com.grocery.entity.Cart;
import com.grocery.entity.CartItem;
import com.grocery.entity.Order;
import com.grocery.entity.OrderItem;
import com.grocery.entity.Product;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CartRepository;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.UserRepository;

import com.grocery.dto.UpdateOrderStatusRequest;
import com.grocery.entity.OrderStatus;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Place Order
     */
    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty.");
        }

        Order order = new Order();
        order.setCustomer(customer);

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {

            Product product = cartItem.getProduct();

            if (!product.isActive()) {
                throw new BadRequestException(
                        product.getProductName() + " is inactive.");
            }

            if (!product.isAvailable()) {
                throw new BadRequestException(
                        product.getProductName() + " is unavailable.");
            }

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for "
                                + product.getProductName());
            }

            product.setStockQuantity(
                    product.getStockQuantity() - cartItem.getQuantity());

            productRepository.save(product);

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            double subtotal =
                    product.getPrice() * cartItem.getQuantity();

            orderItem.setSubtotal(subtotal);

            totalAmount += subtotal;

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();

        cart.setTotalAmount(0.0);

        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    /**
     * Get Order By Id
     */
    @Override
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        return mapToResponse(order);
    }

    /**
     * Get Customer Orders
     */
    @Override
    public List<OrderResponse> getCustomerOrders(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        return orderRepository.findByCustomer(customer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }
    
    /**
     * Update Order Status
     */
    @Override
    public OrderResponse updateOrderStatus(Long orderId,
                                           UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        OrderStatus newStatus;

        try {

            newStatus = OrderStatus.valueOf(
                    request.getStatus().toUpperCase());

        } catch (IllegalArgumentException ex) {

            throw new BadRequestException("Invalid order status.");
        }

        OrderStatus currentStatus = order.getStatus();

        // Validate status transition
        switch (currentStatus) {

        case PENDING:

            if (newStatus != OrderStatus.CONFIRMED
                    && newStatus != OrderStatus.CANCELLED) {

                throw new BadRequestException(
                        "Order can only move to CONFIRMED or CANCELLED.");
            }

            break;

            case CONFIRMED:

                if (newStatus != OrderStatus.PREPARING) {

                    throw new BadRequestException(
                            "Order can only move to PREPARING.");
                }

                break;

            case PREPARING:

                if (newStatus != OrderStatus.READY_FOR_PICKUP) {

                    throw new BadRequestException(
                            "Order can only move to READY_FOR_PICKUP.");
                }

                break;

            case READY_FOR_PICKUP:

                if (newStatus != OrderStatus.OUT_FOR_DELIVERY) {

                    throw new BadRequestException(
                            "Order can only move to OUT_FOR_DELIVERY.");
                }

                break;

            case OUT_FOR_DELIVERY:

                if (newStatus != OrderStatus.DELIVERED) {

                    throw new BadRequestException(
                            "Order can only move to DELIVERED.");
                }

                break;

            case DELIVERED:

                throw new BadRequestException(
                        "Delivered order cannot be updated.");

            case CANCELLED:

                throw new BadRequestException(
                        "Cancelled order cannot be updated.");

            default:

                throw new BadRequestException(
                        "Invalid order status.");
        }

        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    /**
     * Convert Entity to DTO
     */
    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setOrderDate(order.getOrderDate());

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponse dto = new OrderItemResponse();

            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            dto.setSubtotal(item.getSubtotal());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }

}
