package com.pragma.powerup.ordermicroservice.domain.spi;

import com.pragma.powerup.ordermicroservice.domain.model.DishModel;
import com.pragma.powerup.ordermicroservice.domain.model.RestaurantModel;

public interface IExternalFoodCourtPort {
    RestaurantModel getRestaurant(Long id);
    DishModel getDish(Long id);
}
