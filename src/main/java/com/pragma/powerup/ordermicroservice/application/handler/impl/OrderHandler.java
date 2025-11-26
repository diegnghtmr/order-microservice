package com.pragma.powerup.ordermicroservice.application.handler.impl;

import com.pragma.powerup.ordermicroservice.application.dto.request.AssignChefRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.CreateOrderRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.UpdateOrderStatusRequest;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderResponse;
import com.pragma.powerup.ordermicroservice.application.handler.IOrderHandler;
import com.pragma.powerup.ordermicroservice.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.ordermicroservice.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.ordermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = orderRequestMapper.toOrder(request);
        return orderResponseMapper.toResponse(orderServicePort.createOrder(order));
    }

    @Override
    public OrderResponse assignChef(String orderId, AssignChefRequest request) {
        Order updated = orderServicePort.assignChef(orderId, request.getChefId());
        return orderResponseMapper.toResponse(updated);
    }

    @Override
    public OrderResponse updateStatus(String orderId, UpdateOrderStatusRequest request) {
        Order updated = orderServicePort.updateStatus(orderId, request.getStatus());
        return orderResponseMapper.toResponse(updated);
    }

    @Override
    public OrderResponse getById(String orderId) {
        return orderResponseMapper.toResponse(orderServicePort.getById(orderId));
    }

    @Override
    public List<OrderResponse> getByRestaurantAndStatus(Long restaurantId, String status) {
        return orderServicePort.getByRestaurantAndStatus(restaurantId, status)
                .stream()
                .map(orderResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
