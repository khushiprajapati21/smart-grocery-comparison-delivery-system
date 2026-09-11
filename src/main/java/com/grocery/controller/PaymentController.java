package com.grocery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.PaymentRequest;
import com.grocery.dto.PaymentResponse;
import com.grocery.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Make Payment
     */
    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> makePayment(
            @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.makePayment(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get Payment By Id
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId));
    }

    /**
     * Get Payment By Order Id
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrder(orderId));
    }

    /**
     * Get Customer Payment History
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponse>> getCustomerPayments(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                paymentService.getCustomerPayments(customerId));
    }

}
