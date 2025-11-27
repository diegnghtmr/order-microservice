package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderNotPreparedException extends RuntimeException {
    public OrderNotPreparedException(String message) {
        super(message);
    }
}
