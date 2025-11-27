package com.pragma.powerup.ordermicroservice.domain.api;

import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderPage;

import java.util.List;

public interface IOrderServicePort {
    Order createOrder(Order order);

    Order assignChef(String orderId, Long chefId);

    Order updateStatus(String orderId, String status);

    Order getById(String orderId);

    List<Order> getByRestaurantAndStatus(Long restaurantId, String status);

    OrderPage getAllOrdersByStatus(Integer page, Integer size, String status, Long restaurantId);

    Order assignOrder(String orderId, Long employeeId, Long restaurantId, String employeeEmail);

    Order markOrderReady(String orderId, Long employeeId, Long restaurantId, String employeeEmail);

    void deliverOrder(String orderId, String pin, String employeeEmail);

    void cancelOrder(String orderId, Long userId);
}