package com.grocery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocery.entity.Shop;
import com.grocery.entity.User;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByOwner(User owner);

    Optional<Long> findIdByShopName(String shopName);

    boolean existsByShopName(String shopName);

    List<Shop> findByActiveTrue();

    List<Shop> findByCityAndActiveTrue(String city);

    long countByActiveTrue();
}