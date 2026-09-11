package com.grocery.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.dto.AdminDashboardResponse;
import com.grocery.service.AdminService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.grocery.dto.RegisterRequest;
import com.grocery.entity.User;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Dashboard API
     */
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard());
    }
    
    @GetMapping("/dashboard-page")
    public String adminDashboardPage() {
        return "admin/dashboard";
    }
    
    @PostMapping("/shop-owner")
    public ResponseEntity<User> createShopOwner(
            @RequestBody RegisterRequest request) {

        User shopOwner = adminService.createShopOwner(request);

        return ResponseEntity.ok(shopOwner);
    }
    
    @GetMapping("/admin/orders")
    public String adminOrdersPage() {
        return "admin/orders";
    }
    
    @GetMapping("/create-shop-owner")
    public String createShopOwnerPage() {
        return "admin/create-shop-owner";
    }

}