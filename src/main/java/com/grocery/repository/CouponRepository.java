package com.grocery.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocery.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * Find coupon by code
     */
    Optional<Coupon> findByCouponCode(String couponCode);

    /**
     * Check duplicate coupon code
     */
    boolean existsByCouponCode(String couponCode);

    /**
     * Get all active coupons
     */
    List<Coupon> findByActiveTrue();

    /**
     * Find valid coupon
     */
    Optional<Coupon> findByCouponCodeAndActiveTrue(String couponCode);

    /**
     * Get expired coupons
     */
    List<Coupon> findByExpiryDateBefore(LocalDate date);

}
