package com.grocery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.InventoryResponse;
import com.grocery.dto.ProductResponse;
import com.grocery.dto.StockUpdateRequest;
import com.grocery.entity.Product;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.ProductRepository;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private static final int LOW_STOCK_LIMIT = 10;

    private final ProductRepository productRepository;

    public InventoryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Inventory Summary
     */
    @Override
    public InventoryResponse getInventorySummary() {

        InventoryResponse response = new InventoryResponse();

        response.setTotalProducts(
                productRepository.countByActiveTrue());

        response.setAvailableProducts(
                productRepository.countByAvailableTrue());

        response.setLowStockProducts(
                productRepository.countByStockQuantityLessThanAndActiveTrue(
                        LOW_STOCK_LIMIT));

        response.setOutOfStockProducts(
                productRepository.countByStockQuantityAndActiveTrue(0));

        return response;
    }

    /**
     * Available Products
     */
    @Override
    public List<ProductResponse> getAvailableProducts() {

        return productRepository.findByAvailableTrueAndActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Low Stock Products
     */
    @Override
    public List<ProductResponse> getLowStockProducts() {

        return productRepository
                .findByStockQuantityLessThanAndActiveTrue(LOW_STOCK_LIMIT)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Out Of Stock Products
     */
    @Override
    public List<ProductResponse> getOutOfStockProducts() {

        return productRepository
                .findByStockQuantityAndActiveTrue(0)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update Product Stock
     */
    @Override
    public ProductResponse updateStock(Long productId,
                                       StockUpdateRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        if (request.getStockQuantity() < 0) {
            throw new BadRequestException(
                    "Stock quantity cannot be negative.");
        }

        product.setStockQuantity(request.getStockQuantity());

        product.setAvailable(request.getStockQuantity() > 0);

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    /**
     * Product Entity -> ProductResponse
     */
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setUnit(product.getUnit());

        response.setAvailable(product.getAvailable());
        response.setActive(product.getActive());
        response.setCreatedAt(product.getCreatedAt());

        response.setShopId(product.getShop().getId());
        response.setShopName(product.getShop().getShopName());

        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getCategoryName());

        return response;
    }
}