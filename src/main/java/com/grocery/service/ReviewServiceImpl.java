package com.grocery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.ReviewRequest;
import com.grocery.dto.ReviewResponse;
import com.grocery.entity.Product;
import com.grocery.entity.Review;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.ReviewRepository;
import com.grocery.repository.UserRepository;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Add Review
     */
    @Override
    public ReviewResponse addReview(ReviewRequest request) {

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        if (!product.getActive()) {
            throw new BadRequestException("Product is inactive.");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5.");
        }

        if (reviewRepository.findByCustomerAndProduct(customer, product).isPresent()) {
            throw new DuplicateResourceException(
                    "You have already reviewed this product.");
        }

        Review review = new Review();

        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setReview(request.getReview());

        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    /**
     * Update Review
     */
    @Override
    public ReviewResponse updateReview(Long reviewId,
                                       ReviewRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found."));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5.");
        }

        review.setRating(request.getRating());
        review.setReview(request.getReview());

        Review updatedReview = reviewRepository.save(review);

        return mapToResponse(updatedReview);
    }

    /**
     * Delete Review
     */
    @Override
    public void deleteReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found."));

        reviewRepository.delete(review);
    }

    /**
     * Get Review By Id
     */
    @Override
    public ReviewResponse getReviewById(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found."));

        return mapToResponse(review);
    }

    /**
     * Get Reviews By Product
     */
    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        return reviewRepository.findByProduct(product)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get Customer Reviews
     */
    @Override
    public List<ReviewResponse> getCustomerReviews(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        return reviewRepository.findByCustomer(customer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> DTO
     */
    private ReviewResponse mapToResponse(Review review) {

        ReviewResponse response = new ReviewResponse();

        response.setReviewId(review.getId());

        response.setCustomerId(review.getCustomer().getId());
        response.setCustomerName(review.getCustomer().getName());

        response.setProductId(review.getProduct().getId());
        response.setProductName(review.getProduct().getProductName());

        response.setRating(review.getRating());
        response.setReview(review.getReview());
        response.setReviewDate(review.getReviewDate());

        return response;
    }

}
