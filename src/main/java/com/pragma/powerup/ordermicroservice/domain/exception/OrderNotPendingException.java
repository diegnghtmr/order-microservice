package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderNotPendingException extends RuntimeException {
    public OrderNotPendingException(String message) {
        super(message);
    }
}
