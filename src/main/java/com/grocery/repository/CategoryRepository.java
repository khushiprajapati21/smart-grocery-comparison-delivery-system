package com.grocery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocery.entity.Category;
import com.grocery.entity.Shop;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Duplicate category in same shop
    boolean existsByCategoryNameAndShop(String categoryName, Shop shop);

    // Find active category by id
    Optional<Category> findByIdAndActiveTrue(Long id);

    // Get all active categories
    List<Category> findByActiveTrue();

    // Get categories of a shop
    List<Category> findByShopAndActiveTrue(Shop shop);

    // Count active categories
    long countByActiveTrue();
    
    long countByShop(Shop shop);
}