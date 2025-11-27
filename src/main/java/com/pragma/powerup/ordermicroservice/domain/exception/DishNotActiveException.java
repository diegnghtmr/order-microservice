package com.pragma.powerup.ordermicroservice.domain.exception;

public class DishNotActiveException extends RuntimeException {
    public DishNotActiveException(Long id) {
        super("Dish is not active: " + id);
    }
}
