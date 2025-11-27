package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderAlreadyAssignedException extends RuntimeException {
    public OrderAlreadyAssignedException(String message) {
        super(message);
    }
}
