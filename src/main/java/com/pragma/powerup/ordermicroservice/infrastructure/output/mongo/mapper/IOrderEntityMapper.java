package com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.mapper;

import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity.OrderDishEntity;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity.OrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderEntityMapper {

    OrderEntity toEntity(Order order);

    Order toDomain(OrderEntity entity);

    OrderDishEntity toDishEntity(OrderDish dish);

    OrderDish toDishDomain(OrderDishEntity entity);

    List<OrderDishEntity> toDishEntityList(List<OrderDish> dishes);

    List<OrderDish> toDishDomainList(List<OrderDishEntity> entities);
}
