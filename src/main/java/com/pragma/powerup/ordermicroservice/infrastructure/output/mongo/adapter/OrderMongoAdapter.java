package com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.adapter;

import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity.OrderEntity;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.mapper.IOrderEntityMapper;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMongoAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderEntityMapper.toEntity(order);
        OrderEntity saved = orderRepository.save(entity);
        return orderEntityMapper.toDomain(saved);
    }

    @Override
    public Order update(Order order) {
        OrderEntity entity = orderEntityMapper.toEntity(order);
        OrderEntity saved = orderRepository.save(entity);
        return orderEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id).map(orderEntityMapper::toDomain);
    }

    @Override
    public List<Order> findByRestaurantIdAndStatus(Long restaurantId, String status) {
        return orderRepository.findByRestaurantIdAndStatus(restaurantId, status)
                .stream()
                .map(orderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
