package com.pragma.powerup.ordermicroservice.application.mapper;

import com.pragma.powerup.ordermicroservice.application.dto.request.CreateOrderRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.OrderDishRequest;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "chefId", ignore = true)
    Order toOrder(CreateOrderRequest request);

    OrderDish toDish(OrderDishRequest request);

    List<OrderDish> toDishList(List<OrderDishRequest> requests);
}
