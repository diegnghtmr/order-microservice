package com.pragma.powerup.ordermicroservice.domain.exception;

public class DishBelongsToAnotherRestaurantException extends RuntimeException {
    public DishBelongsToAnotherRestaurantException(Long dishId, Long restaurantId) {
        super("Dish " + dishId + " does not belong to restaurant " + restaurantId);
    }
}
