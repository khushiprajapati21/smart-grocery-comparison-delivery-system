package com.grocery.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.PaymentRequest;
import com.grocery.dto.PaymentResponse;
import com.grocery.entity.Order;
import com.grocery.entity.Payment;
import com.grocery.entity.PaymentMethod;
import com.grocery.entity.PaymentStatus;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.PaymentRepository;
import com.grocery.repository.UserRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              UserRepository userRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    /**
     * Make Payment
     */
    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException(
                    "Order does not belong to this customer.");
        }

        if (paymentRepository.findByOrder(order).isPresent()) {
            throw new BadRequestException(
                    "Payment already exists for this order.");
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setCustomer(customer);
        payment.setAmount(order.getTotalAmount());

        PaymentMethod method =
                PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());

        payment.setPaymentMethod(method);

        if (method == PaymentMethod.CASH_ON_DELIVERY) {

            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setTransactionId(null);

        } else {

            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(
                    UUID.randomUUID().toString());
        }

        Payment savedPayment = paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }

    /**
     * Get Payment By Id
     */
    @Override
    public PaymentResponse getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found."));

        return mapToResponse(payment);
    }

    /**
     * Get Payment By Order
     */
    @Override
    public PaymentResponse getPaymentByOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found."));

        return mapToResponse(payment);
    }

    /**
     * Customer Payment History
     */
    @Override
    public List<PaymentResponse> getCustomerPayments(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        return paymentRepository.findByCustomer(customer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> DTO
     */
    private PaymentResponse mapToResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setPaymentId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setCustomerId(payment.getCustomer().getId());
        response.setCustomerName(payment.getCustomer().getName());

        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod().name());
        response.setPaymentStatus(payment.getPaymentStatus().name());
        response.setTransactionId(payment.getTransactionId());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }
}
