package com.pragma.powerup.ordermicroservice.domain.api;

import com.pragma.powerup.ordermicroservice.domain.model.Order;

import java.util.List;

public interface IOrderServicePort {
    Order createOrder(Order order);

    Order assignChef(String orderId, Long chefId);

    Order updateStatus(String orderId, String status);

    Order getById(String orderId);

    List<Order> getByRestaurantAndStatus(Long restaurantId, String status);
}
