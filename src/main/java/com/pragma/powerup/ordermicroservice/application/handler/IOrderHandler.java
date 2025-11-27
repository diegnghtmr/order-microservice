package com.pragma.powerup.ordermicroservice.application.handler;

import com.pragma.powerup.ordermicroservice.application.dto.request.AssignChefRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.CreateOrderRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.UpdateOrderStatusRequest;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderResponse;

import com.pragma.powerup.ordermicroservice.application.dto.response.OrderPageResponse;

import java.util.List;

public interface IOrderHandler {
    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse assignChef(String orderId, AssignChefRequest request);

    OrderResponse updateStatus(String orderId, UpdateOrderStatusRequest request);

    OrderResponse getById(String orderId);

    List<OrderResponse> getByRestaurantAndStatus(Long restaurantId, String status);

    OrderPageResponse getAllOrdersByStatus(Integer page, Integer size, String status);

    OrderResponse assignOrder(String orderId);

    OrderResponse markOrderReady(String orderId);

    void deliverOrder(String orderId, String pin);

    void cancelOrder(String orderId);
}
