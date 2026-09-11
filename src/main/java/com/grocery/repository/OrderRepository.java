package com.grocery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grocery.entity.Order;
import com.grocery.entity.OrderStatus;
import com.grocery.entity.Shop;
import com.grocery.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ===========================
    // Existing Methods
    // ===========================

    List<Order> findByCustomer(User customer);

    List<Order> findByStatus(OrderStatus status);

    long countByStatus(OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o")
    Double getTotalRevenue();

    // ===========================
    // Shop Owner Dashboard
    // ===========================

    List<Order> findByOrderItemsProductShop(Shop shop);

    long countByOrderItemsProductShop(Shop shop);

    long countByOrderItemsProductShopAndStatus(
            Shop shop,
            OrderStatus status);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount),0)
            FROM Order o
            JOIN o.orderItems oi
            WHERE oi.product.shop = :shop
            """)
    Double getTotalRevenueByShop(@Param("shop") Shop shop);

    // ===========================
    // Customer Dashboard
    // ===========================

    long countByCustomer(User customer);

    long countByCustomerAndStatus(
            User customer,
            OrderStatus status);

    List<Order> findTop5ByCustomerOrderByOrderDateDesc(
            User customer);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount),0)
            FROM Order o
            WHERE o.customer = :customer
            """)
    Double getTotalSpent(
            @Param("customer") User customer);

}