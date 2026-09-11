package com.grocery.service;

import java.util.List;

import com.grocery.dto.CouponRequest;
import com.grocery.dto.CouponResponse;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);

    CouponResponse updateCoupon(Long couponId, CouponRequest request);

    void deleteCoupon(Long couponId);

    CouponResponse getCouponById(Long couponId);

    List<CouponResponse> getAllCoupons();

    Double applyCoupon(String couponCode, Double orderAmount);

}
