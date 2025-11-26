package com.pragma.powerup.ordermicroservice.domain.exception;

public class InvalidClientException extends RuntimeException {
    public InvalidClientException(Long clientId) {
        super("Client does not exist: " + clientId);
    }
}
