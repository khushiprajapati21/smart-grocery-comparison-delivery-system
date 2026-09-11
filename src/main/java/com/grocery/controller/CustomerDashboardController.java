package com.grocery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.CustomerDashboardResponse;
import com.grocery.service.CustomerDashboardService;

@RestController
@RequestMapping("/customer")
public class CustomerDashboardController {

    private final CustomerDashboardService customerDashboardService;

    public CustomerDashboardController(
            CustomerDashboardService customerDashboardService) {

        this.customerDashboardService = customerDashboardService;
    }

    /**
     * Customer Dashboard
     */
    @GetMapping("/dashboard/{customerId}")
    public ResponseEntity<CustomerDashboardResponse> getDashboard(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                customerDashboardService.getDashboard(customerId));
    }

}
