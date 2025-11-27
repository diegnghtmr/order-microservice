package com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.repository;

import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IOrderRepository extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByRestaurantIdAndStatus(Long restaurantId, String status);

    boolean existsByClientIdAndStatusIn(Long clientId, List<String> statuses);
}
