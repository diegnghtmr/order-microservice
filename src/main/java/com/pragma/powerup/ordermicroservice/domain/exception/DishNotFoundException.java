package com.pragma.powerup.ordermicroservice.domain.exception;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(Long id) {
        super("Dish not found: " + id);
    }
}
