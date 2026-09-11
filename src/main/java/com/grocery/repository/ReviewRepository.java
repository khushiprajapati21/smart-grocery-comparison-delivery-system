package com.grocery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grocery.entity.Product;
import com.grocery.entity.Review;
import com.grocery.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Check if customer already reviewed this product
     */
    Optional<Review> findByCustomerAndProduct(User customer, Product product);

    /**
     * Get all reviews of a product
     */
    List<Review> findByProduct(Product product);

    /**
     * Get all reviews given by a customer
     */
    List<Review> findByCustomer(User customer);

    /**
     * Count reviews of a product
     */
    long countByProduct(Product product);

    /**
     * Average rating of a product
     */
    @Query("SELECT COALESCE(AVG(r.rating),0) FROM Review r WHERE r.product = :product")
    Double getAverageRating(Product product);

}
