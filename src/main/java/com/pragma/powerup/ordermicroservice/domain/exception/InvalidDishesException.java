package com.pragma.powerup.ordermicroservice.domain.exception;

public class InvalidDishesException extends RuntimeException {
    public InvalidDishesException(Long restaurantId) {
        super("Provided dishes do not belong to restaurant: " + restaurantId);
    }
}
