package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderBelongsToAnotherRestaurantException extends RuntimeException {
    public OrderBelongsToAnotherRestaurantException(String message) {
        super(message);
    }
}
