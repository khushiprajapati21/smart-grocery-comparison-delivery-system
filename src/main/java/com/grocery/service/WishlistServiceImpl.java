package com.grocery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocery.dto.AddToWishlistRequest;
import com.grocery.dto.WishlistItemResponse;
import com.grocery.dto.WishlistResponse;
import com.grocery.entity.Product;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.entity.Wishlist;
import com.grocery.entity.WishlistItem;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.UserRepository;
import com.grocery.repository.WishlistItemRepository;
import com.grocery.repository.WishlistRepository;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository,
                               WishlistItemRepository wishlistItemRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistResponse addToWishlist(AddToWishlistRequest request) {

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        Wishlist wishlist = wishlistRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setCustomer(customer);
                    return wishlistRepository.save(newWishlist);
                });

        if (wishlistItemRepository
                .findByWishlistAndProduct(wishlist, product)
                .isPresent()) {

            throw new DuplicateResourceException(
                    "Product already exists in wishlist.");
        }

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);

        wishlistItemRepository.save(item);

        return mapToResponse(wishlist);
    }

    @Override
    public WishlistResponse getWishlist(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Wishlist wishlist = wishlistRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist is empty."));

        return mapToResponse(wishlist);
    }

    @Override
    public void removeProduct(Long customerId,
                              Long productId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Wishlist wishlist = wishlistRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        WishlistItem item = wishlistItemRepository
                .findByWishlistAndProduct(wishlist, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found in wishlist."));

        wishlistItemRepository.delete(item);
    }

    @Override
    public void clearWishlist(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Wishlist wishlist = wishlistRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wishlist not found."));

        wishlistItemRepository.deleteByWishlist(wishlist);
    }

    private WishlistResponse mapToResponse(Wishlist wishlist) {

        WishlistResponse response = new WishlistResponse();

        response.setWishlistId(wishlist.getId());
        response.setCustomerId(wishlist.getCustomer().getId());
        response.setCustomerName(wishlist.getCustomer().getName());
        response.setCreatedAt(wishlist.getCreatedAt());

        List<WishlistItemResponse> items = new ArrayList<>();

        for (WishlistItem item :
                wishlistItemRepository.findByWishlist(wishlist)) {

            WishlistItemResponse dto =
                    new WishlistItemResponse();

            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setStockQuantity(item.getProduct().getStockQuantity());
            dto.setUnit(item.getProduct().getUnit());
            dto.setAvailable(item.getProduct().getAvailable());

            items.add(dto);
        }

        response.setItems(items);

        return response;
    }
}
