package com.pragma.powerup.ordermicroservice.application.mapper;

import com.pragma.powerup.ordermicroservice.application.dto.response.OrderDishResponse;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderResponse;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IOrderResponseMapper {
    OrderResponse toResponse(Order order);

    OrderDishResponse toDishResponse(OrderDish dish);

    List<OrderDishResponse> toDishResponseList(List<OrderDish> dishes);
}
