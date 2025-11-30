package com.pragma.powerup.ordermicroservice.domain.spi;

public interface ITraceabilityPort {
    void logTrace(String orderId, Long clientId, String employeeEmail, String previousState, String newState, Long restaurantId);
}
