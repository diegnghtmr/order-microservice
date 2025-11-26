package com.pragma.powerup.ordermicroservice.domain.model;

import java.util.Objects;

public class OrderDish {
    private Long dishId;
    private Integer quantity;

    public OrderDish() {
    }

    public OrderDish(Long dishId, Integer quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderDish orderDish = (OrderDish) o;
        return Objects.equals(dishId, orderDish.dishId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId);
    }
}
