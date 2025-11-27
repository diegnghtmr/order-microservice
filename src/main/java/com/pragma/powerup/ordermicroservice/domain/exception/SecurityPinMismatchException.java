package com.pragma.powerup.ordermicroservice.domain.exception;

public class SecurityPinMismatchException extends RuntimeException {
    public SecurityPinMismatchException() {
        super("Security PIN mismatch");
    }
}
