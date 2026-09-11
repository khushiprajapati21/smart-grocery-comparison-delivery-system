package com.grocery.service;

import org.springframework.stereotype.Service;

import com.grocery.dto.ShopOwnerDashboardResponse;
import com.grocery.entity.OrderStatus;
import com.grocery.entity.Role;
import com.grocery.entity.Shop;
import com.grocery.entity.User;
import com.grocery.exception.BadRequestException;
import com.grocery.exception.ResourceNotFoundException;
import com.grocery.repository.CategoryRepository;
import com.grocery.repository.OrderRepository;
import com.grocery.repository.ProductRepository;
import com.grocery.repository.ShopRepository;
import com.grocery.repository.UserRepository;

@Service
public class ShopOwnerServiceImpl implements ShopOwnerService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ShopOwnerServiceImpl(UserRepository userRepository,
                                ShopRepository shopRepository,
                                CategoryRepository categoryRepository,
                                ProductRepository productRepository,
                                OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ShopOwnerDashboardResponse getDashboard(Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop owner not found."));

        if (owner.getRole() != Role.SHOP_OWNER) {
            throw new BadRequestException("User is not a shop owner.");
        }

        Shop shop = shopRepository.findByOwner(owner)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shop not found."));

        ShopOwnerDashboardResponse response =
                new ShopOwnerDashboardResponse();

        response.setShopId(shop.getId());
        response.setShopName(shop.getShopName());

        response.setTotalCategories(
                categoryRepository.countByShop(shop));

        response.setTotalProducts(
                productRepository.countByShop(shop));

        response.setActiveProducts(
                productRepository.countByShopAndActiveTrue(shop));

        response.setOutOfStockProducts(
                productRepository.countByShopAndStockQuantity(shop, 0));

        response.setTotalOrders(
                orderRepository.countByOrderItemsProductShop(shop));

        response.setPendingOrders(
                orderRepository.countByOrderItemsProductShopAndStatus(
                        shop,
                        OrderStatus.PENDING));

        response.setPreparingOrders(
                orderRepository.countByOrderItemsProductShopAndStatus(
                        shop,
                        OrderStatus.PREPARING));

        response.setReadyForPickupOrders(
                orderRepository.countByOrderItemsProductShopAndStatus(
                        shop,
                        OrderStatus.READY_FOR_PICKUP));

        response.setDeliveredOrders(
                orderRepository.countByOrderItemsProductShopAndStatus(
                        shop,
                        OrderStatus.DELIVERED));

        Double revenue = orderRepository.getTotalRevenueByShop(shop);

        response.setTotalRevenue(
                revenue == null ? 0.0 : revenue);

        return response;
    }
}
