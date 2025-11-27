package com.pragma.powerup.ordermicroservice.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotNull
    private Long restaurantId;

    @NotEmpty
    @Valid
    private List<OrderDishRequest> dishes;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<OrderDishRequest> getDishes() {
        return dishes;
    }

    public void setDishes(List<OrderDishRequest> dishes) {
        this.dishes = dishes;
    }
}
