package com.pragma.powerup.ordermicroservice.domain.exception;

public class ClientHasActiveOrderException extends RuntimeException {
    public ClientHasActiveOrderException(Long clientId) {
        super("Client has an active order: " + clientId);
    }
}
