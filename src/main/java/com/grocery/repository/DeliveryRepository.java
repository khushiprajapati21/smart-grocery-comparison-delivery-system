package com.grocery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grocery.entity.Delivery;
import com.grocery.entity.Order;
import com.grocery.entity.User;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrder(Order order);

    List<Delivery> findByDeliveryAgent(User deliveryAgent);

    boolean existsByOrder(Order order);
}
