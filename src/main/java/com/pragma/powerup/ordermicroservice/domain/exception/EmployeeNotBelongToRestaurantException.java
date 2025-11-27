package com.pragma.powerup.ordermicroservice.domain.exception;

public class EmployeeNotBelongToRestaurantException extends RuntimeException {
    public EmployeeNotBelongToRestaurantException() {
        super("Employee does not belong to any restaurant.");
    }
}
