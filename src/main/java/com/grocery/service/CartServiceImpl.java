package com.grocery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.grocery.dto.AddToCartRequest;
import com.grocery.dto.CartItemResponse;
import com.grocery.dto.CartResponse;
import com.grocery.entity.Cart;
import com.grocery.entity.CartItem;
import com.grocery.entity.Product;
import com.grocery.entity.Role;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CartItemRepository;
import com.grocery.repository.CartRepository;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Add Product To Cart
     */
    @Override
    public CartResponse addToCart(AddToCartRequest request) {

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("User is not a customer.");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        if (!product.isActive()) {
            throw new BadRequestException("Product is inactive.");
        }

        if (!product.isAvailable()) {
            throw new BadRequestException("Product is unavailable.");
        }

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero.");
        }

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new BadRequestException("Insufficient stock available.");
        }

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    newCart.setTotalAmount(0.0);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(request.getQuantity());

        } else {

            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStockQuantity()) {
                throw new BadRequestException("Insufficient stock available.");
            }

            cartItem.setQuantity(newQuantity);
        }

        cartItem.setSubTotal(cartItem.getPrice() * cartItem.getQuantity());

        cartItemRepository.save(cartItem);

        updateCartTotal(cart);

        return mapToResponse(cart);
    }

    /**
     * Get Cart
     */
    @Override
    public CartResponse getCart(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart is empty."));

        return mapToResponse(cart);
    }

    /**
     * Update Quantity
     */
    @Override
    public CartResponse updateQuantity(Long customerId,
                                       Long productId,
                                       Integer quantity) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found in cart."));

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero.");
        }

        if (quantity > product.getStockQuantity()) {
            throw new BadRequestException("Insufficient stock available.");
        }

        cartItem.setQuantity(quantity);
        cartItem.setSubTotal(quantity * cartItem.getPrice());

        cartItemRepository.save(cartItem);

        updateCartTotal(cart);

        return mapToResponse(cart);
    }

    /**
     * Remove Product
     */
    @Override
    public void removeProduct(Long customerId,
                              Long productId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found in cart."));

        cartItemRepository.delete(cartItem);

        updateCartTotal(cart);
    }

    /**
     * Clear Cart
     */
    @Override
    public void clearCart(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found."));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        cartItemRepository.deleteByCart(cart);

        cart.setTotalAmount(0.0);

        cartRepository.save(cart);
    }

    /**
     * Update Cart Total
     */
    private void updateCartTotal(Cart cart) {

        List<CartItem> items = cartItemRepository.findByCart(cart);

        double total = 0.0;

        for (CartItem item : items) {
            total += item.getSubTotal();
        }

        cart.setTotalAmount(total);

        cartRepository.save(cart);
    }

    /**
     * Entity -> DTO
     */
    private CartResponse mapToResponse(Cart cart) {

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());
        response.setCustomerId(cart.getCustomer().getId());
        response.setCustomerName(cart.getCustomer().getName());
        response.setTotalAmount(cart.getTotalAmount());

        List<CartItemResponse> itemResponses = new ArrayList<>();

        List<CartItem> items = cartItemRepository.findByCart(cart);

        for (CartItem item : items) {

            CartItemResponse dto = new CartItemResponse();

            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getProductName());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setSubTotal(item.getSubTotal());

            itemResponses.add(dto);
        }

        response.setItems(itemResponses);

        return response;
    }

}
