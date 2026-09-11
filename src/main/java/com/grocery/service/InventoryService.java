package com.grocery.service;

import java.util.List;

import com.grocery.dto.InventoryResponse;
import com.grocery.dto.ProductResponse;
import com.grocery.dto.StockUpdateRequest;

public interface InventoryService {

    /**
     * Inventory Summary
     */
    InventoryResponse getInventorySummary();

    /**
     * Available Products
     */
    List<ProductResponse> getAvailableProducts();

    /**
     * Low Stock Products
     */
    List<ProductResponse> getLowStockProducts();

    /**
     * Out Of Stock Products
     */
    List<ProductResponse> getOutOfStockProducts();

    /**
     * Update Stock
     */
    ProductResponse updateStock(Long productId,
                                StockUpdateRequest request);

}