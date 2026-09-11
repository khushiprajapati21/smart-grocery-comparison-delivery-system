package com.grocery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocery.entity.Category;
import com.grocery.entity.Product;
import com.grocery.entity.Shop;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // =========================
    // Product Module
    // =========================

    Optional<Product> findByIdAndActiveTrue(Long id);

    boolean existsByProductNameAndShop(String productName, Shop shop);

    boolean existsByProductNameAndCategory(String productName, Category category);

    List<Product> findByShopAndActiveTrue(Shop shop);

    List<Product> findByCategoryAndActiveTrue(Category category);

    // =========================
    // Dashboard
    // =========================

    List<Product> findByActiveTrue();

    List<Product> findByAvailableTrue();

    long countByActiveTrue();

    long countByAvailableTrue();

    long countByShop(Shop shop);

    long countByShopAndActiveTrue(Shop shop);

    long countByShopAndStockQuantity(Shop shop, Integer stockQuantity);

    // =========================
    // Inventory Module
    // =========================

    // Available products
    List<Product> findByAvailableTrueAndActiveTrue();

    // Low stock products (e.g., stock < 10)
    List<Product> findByStockQuantityLessThanAndActiveTrue(Integer stockQuantity);

    // Out of stock products
    List<Product> findByStockQuantityAndActiveTrue(Integer stockQuantity);

    // Inventory counts
    long countByStockQuantityLessThanAndActiveTrue(Integer stockQuantity);

    long countByStockQuantityAndActiveTrue(Integer stockQuantity);
}