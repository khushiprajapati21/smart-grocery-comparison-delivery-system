package com.grocery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.grocery.dto.ShopRequest;
import com.grocery.dto.ShopResponse;
import com.grocery.entity.Role;
import com.grocery.entity.Shop;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.DuplicateResourceException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.ShopRepository;
import com.grocery.repository.UserRepository;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopServiceImpl(ShopRepository shopRepository,
                           UserRepository userRepository) {

        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create Shop
     */
    @Override
    public ShopResponse createShop(ShopRequest request) {

        // Check duplicate shop name
        if (shopRepository.existsByShopName(request.getShopName())) {
            throw new DuplicateResourceException("Shop name already exists.");
        }

        // Find Owner
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop owner not found."));

        // Verify Owner Role
        if (owner.getRole() != Role.SHOP_OWNER) {
            throw new BadRequestException("Selected user is not a Shop Owner.");
        }

        // One Owner = One Shop
        if (shopRepository.findByOwner(owner).isPresent()) {
            throw new DuplicateResourceException("This owner already has a registered shop.");
        }

        Shop shop = new Shop();

        shop.setShopName(request.getShopName());
        shop.setPhone(request.getPhone());
        shop.setAddress(request.getAddress());
        shop.setCity(request.getCity());
        shop.setState(request.getState());
        shop.setPincode(request.getPincode());
        shop.setOpeningTime(request.getOpeningTime());
        shop.setClosingTime(request.getClosingTime());

        shop.setOwner(owner);
        shop.setActive(true);

        Shop savedShop = shopRepository.save(shop);

        return mapToResponse(savedShop);
    }

    /**
     * Get Shop By Id
     */
    @Override
    public ShopResponse getShopById(Long id) {

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        return mapToResponse(shop);
    }

    /**
     * Get All Shops
     */
    @Override
    public List<ShopResponse> getAllShops() {

        return shopRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update Shop
     */
    @Override
    public ShopResponse updateShop(Long id, ShopRequest request) {

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        // Check duplicate shop name (only if changed)
        if (!shop.getShopName().equals(request.getShopName())
                && shopRepository.existsByShopName(request.getShopName())) {
            throw new DuplicateResourceException("Shop name already exists.");
        }

        // Fetch owner from database
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop owner not found."));

        // Validate role
        if (owner.getRole() != Role.SHOP_OWNER) {
            throw new BadRequestException("Selected user is not a Shop Owner.");
        }

        // If changing owner, ensure the new owner doesn't already own another shop
        if (!shop.getOwner().getId().equals(owner.getId())) {

            shopRepository.findByOwner(owner).ifPresent(existingShop -> {
                throw new DuplicateResourceException(
                        "This owner already has a registered shop.");
            });

            shop.setOwner(owner);
        }

        shop.setShopName(request.getShopName());
        shop.setPhone(request.getPhone());
        shop.setAddress(request.getAddress());
        shop.setCity(request.getCity());
        shop.setState(request.getState());
        shop.setPincode(request.getPincode());
        shop.setOpeningTime(request.getOpeningTime());
        shop.setClosingTime(request.getClosingTime());

        Shop updatedShop = shopRepository.save(shop);

        return mapToResponse(updatedShop);
    }

    /**
     * Soft Delete Shop
     */
    @Override
    public void deleteShop(Long id) {

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        shop.setActive(false);

        shopRepository.save(shop);
    }

    /**
     * Get Shops By City
     */
    @Override
    public List<ShopResponse> getShopsByCity(String city) {

        return shopRepository.findByCityAndActiveTrue(city)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convert Shop Entity to ShopResponse DTO
     */
    private ShopResponse mapToResponse(Shop shop) {

        ShopResponse response = new ShopResponse();

        response.setId(shop.getId());
        response.setShopName(shop.getShopName());

        // Owner Details
        response.setOwnerName(shop.getOwner().getName());
        response.setEmail(shop.getOwner().getEmail());

        // Shop Details
        response.setPhone(shop.getPhone());
        response.setAddress(shop.getAddress());
        response.setCity(shop.getCity());
        response.setState(shop.getState());
        response.setPincode(shop.getPincode());
        response.setOpeningTime(shop.getOpeningTime());
        response.setClosingTime(shop.getClosingTime());

        response.setActive(shop.isActive());
        response.setCreatedAt(shop.getCreatedAt());

        return response;
    }
}