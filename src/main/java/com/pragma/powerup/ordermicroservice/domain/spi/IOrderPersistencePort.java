package com.pragma.powerup.ordermicroservice.domain.spi;

import com.pragma.powerup.ordermicroservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order save(Order order);

    Order update(Order order);

    Optional<Order> findById(String id);

    List<Order> findByRestaurantIdAndStatus(Long restaurantId, String status);
}
