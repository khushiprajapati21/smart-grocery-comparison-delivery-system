package com.grocery.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.InventoryResponse;
import com.grocery.dto.ProductResponse;
import com.grocery.dto.StockUpdateRequest;
import com.grocery.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Inventory Summary
     */
    @GetMapping("/summary")
    public ResponseEntity<InventoryResponse> getInventorySummary() {

        return ResponseEntity.ok(
                inventoryService.getInventorySummary());
    }

    /**
     * Available Products
     */
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {

        return ResponseEntity.ok(
                inventoryService.getAvailableProducts());
    }

    /**
     * Low Stock Products
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {

        return ResponseEntity.ok(
                inventoryService.getLowStockProducts());
    }

    /**
     * Out Of Stock Products
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<ProductResponse>> getOutOfStockProducts() {

        return ResponseEntity.ok(
                inventoryService.getOutOfStockProducts());
    }

    /**
     * Update Product Stock
     */
    @PutMapping("/product/{productId}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long productId,
            @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                inventoryService.updateStock(productId, request));
    }

}