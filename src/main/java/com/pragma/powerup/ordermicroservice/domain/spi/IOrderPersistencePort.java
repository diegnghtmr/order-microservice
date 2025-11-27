package com.pragma.powerup.ordermicroservice.domain.spi;

import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderPage;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, String status);
    Order update(Order order);
    boolean existsActiveOrder(Long clientId);
    OrderPage findByRestaurantIdAndStatus(Long restaurantId, String status, Integer page, Integer size);
}