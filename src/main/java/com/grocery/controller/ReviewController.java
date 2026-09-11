package com.grocery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grocery.dto.ReviewRequest;
import com.grocery.dto.ReviewResponse;
import com.grocery.service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Add Review
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody ReviewRequest request) {

        ReviewResponse response = reviewService.addReview(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update Review
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, request));
    }

    /**
     * Delete Review
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok("Review deleted successfully.");
    }

    /**
     * Get Review By Id
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(
                reviewService.getReviewById(reviewId));
    }

    /**
     * Get Reviews Of Product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProduct(productId));
    }

    /**
     * Get Customer Reviews
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                reviewService.getCustomerReviews(customerId));
    }

}
