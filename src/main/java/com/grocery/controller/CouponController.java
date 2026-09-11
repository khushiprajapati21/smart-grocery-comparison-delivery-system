package com.grocery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.CouponRequest;
import com.grocery.dto.CouponResponse;
import com.grocery.service.CouponService;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * Create Coupon
     */
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @RequestBody CouponRequest request) {

        CouponResponse response = couponService.createCoupon(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update Coupon
     */
    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponse> updateCoupon(
            @PathVariable Long couponId,
            @RequestBody CouponRequest request) {

        return ResponseEntity.ok(
                couponService.updateCoupon(couponId, request));
    }

    /**
     * Delete Coupon (Soft Delete)
     */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCoupon(
            @PathVariable Long couponId) {

        couponService.deleteCoupon(couponId);

        return ResponseEntity.ok("Coupon deleted successfully.");
    }

    /**
     * Get Coupon By Id
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCouponById(
            @PathVariable Long couponId) {

        return ResponseEntity.ok(
                couponService.getCouponById(couponId));
    }

    /**
     * Get All Coupons
     */
    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {

        return ResponseEntity.ok(
                couponService.getAllCoupons());
    }

    /**
     * Apply Coupon
     */
    @GetMapping("/apply")
    public ResponseEntity<Double> applyCoupon(

            @RequestParam String couponCode,
            @RequestParam Double orderAmount) {

        return ResponseEntity.ok(
                couponService.applyCoupon(couponCode, orderAmount));
    }

}
