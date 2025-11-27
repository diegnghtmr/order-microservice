package com.pragma.powerup.ordermicroservice.domain.exception;

public class OrderNotBelongToClientException extends RuntimeException {
    public OrderNotBelongToClientException(String message) {
        super(message);
    }
}
