package com.grocery.service;

import java.util.List;

import com.grocery.dto.PaymentRequest;
import com.grocery.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long paymentId);

    PaymentResponse getPaymentByOrder(Long orderId);

    List<PaymentResponse> getCustomerPayments(Long customerId);

}
