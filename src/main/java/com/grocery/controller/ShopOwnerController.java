package com.grocery.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.ShopOwnerDashboardResponse;
import com.grocery.service.ShopOwnerService;

@RestController
@RequestMapping("/shop-owner")
public class ShopOwnerController {

    private final ShopOwnerService shopOwnerService;

    public ShopOwnerController(ShopOwnerService shopOwnerService) {
        this.shopOwnerService = shopOwnerService;
    }

    /**
     * Shop Owner Dashboard
     */
    @GetMapping("/dashboard/{ownerId}")
    public ResponseEntity<ShopOwnerDashboardResponse> getDashboard(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                shopOwnerService.getDashboard(ownerId));
    }

}
