package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderNotReadyException extends RuntimeException {
    public OrderNotReadyException(String message) {
        super(message);
    }
}
