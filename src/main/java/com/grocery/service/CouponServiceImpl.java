package com.grocery.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.CouponRequest;
import com.grocery.dto.CouponResponse;
import com.grocery.entity.Coupon;
import com.grocery.entity.DiscountType;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CouponRepository;

@Service
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * Create Coupon
     */
    @Override
    public CouponResponse createCoupon(CouponRequest request) {

        if (couponRepository.existsByCouponCode(request.getCouponCode())) {
            throw new DuplicateResourceException("Coupon code already exists.");
        }

        validateCoupon(request);

        Coupon coupon = new Coupon();

        coupon.setCouponCode(request.getCouponCode().toUpperCase());
        coupon.setDiscountType(
                DiscountType.valueOf(request.getDiscountType().toUpperCase()));
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setActive(true);

        return mapToResponse(couponRepository.save(coupon));
    }

    /**
     * Update Coupon
     */
    @Override
    public CouponResponse updateCoupon(Long couponId,
                                       CouponRequest request) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found."));

        if (!coupon.getCouponCode().equalsIgnoreCase(request.getCouponCode())
                && couponRepository.existsByCouponCode(request.getCouponCode())) {

            throw new DuplicateResourceException(
                    "Coupon code already exists.");
        }

        validateCoupon(request);

        coupon.setCouponCode(request.getCouponCode().toUpperCase());
        coupon.setDiscountType(
                DiscountType.valueOf(request.getDiscountType().toUpperCase()));
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumOrderAmount(request.getMinimumOrderAmount());
        coupon.setExpiryDate(request.getExpiryDate());

        return mapToResponse(couponRepository.save(coupon));
    }

    /**
     * Soft Delete Coupon
     */
    @Override
    public void deleteCoupon(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found."));

        coupon.setActive(false);

        couponRepository.save(coupon);
    }

    /**
     * Get Coupon By Id
     */
    @Override
    public CouponResponse getCouponById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found."));

        return mapToResponse(coupon);
    }

    /**
     * Get All Coupons
     */
    @Override
    public List<CouponResponse> getAllCoupons() {

        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Apply Coupon
     */
    @Override
    public Double applyCoupon(String couponCode,
                              Double orderAmount) {

        Coupon coupon = couponRepository
                .findByCouponCodeAndActiveTrue(couponCode.toUpperCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found."));

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Coupon expired.");
        }

        if (orderAmount < coupon.getMinimumOrderAmount()) {
            throw new BadRequestException(
                    "Minimum order amount is ₹"
                            + coupon.getMinimumOrderAmount());
        }

        double finalAmount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {

            finalAmount = orderAmount -
                    (orderAmount * coupon.getDiscountValue() / 100);

        } else {

            finalAmount = orderAmount -
                    coupon.getDiscountValue();
        }

        return Math.max(finalAmount, 0);
    }

    /**
     * Validation
     */
    private void validateCoupon(CouponRequest request) {

        if (request.getDiscountValue() <= 0) {
            throw new BadRequestException(
                    "Discount value must be greater than zero.");
        }

        if (request.getDiscountType()
                .equalsIgnoreCase("PERCENTAGE")
                && request.getDiscountValue() > 100) {

            throw new BadRequestException(
                    "Percentage discount cannot exceed 100.");
        }

        if (request.getMinimumOrderAmount() < 0) {
            throw new BadRequestException(
                    "Minimum order amount cannot be negative.");
        }

        if (request.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BadRequestException(
                    "Expiry date must be today or a future date.");
        }
    }

    /**
     * Entity -> DTO
     */
    private CouponResponse mapToResponse(Coupon coupon) {

        CouponResponse response = new CouponResponse();

        response.setId(coupon.getId());
        response.setCouponCode(coupon.getCouponCode());
        response.setDiscountType(coupon.getDiscountType().name());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setMinimumOrderAmount(coupon.getMinimumOrderAmount());
        response.setExpiryDate(coupon.getExpiryDate());
        response.setActive(coupon.getActive());

        return response;
    }
}
