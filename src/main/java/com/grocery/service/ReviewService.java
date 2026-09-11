package com.grocery.service;

import java.util.List;

import com.grocery.dto.ReviewRequest;
import com.grocery.dto.ReviewResponse;

public interface ReviewService {

    ReviewResponse addReview(ReviewRequest request);

    ReviewResponse updateReview(Long reviewId, ReviewRequest request);

    void deleteReview(Long reviewId);

    ReviewResponse getReviewById(Long reviewId);

    List<ReviewResponse> getReviewsByProduct(Long productId);

    List<ReviewResponse> getCustomerReviews(Long customerId);

}
