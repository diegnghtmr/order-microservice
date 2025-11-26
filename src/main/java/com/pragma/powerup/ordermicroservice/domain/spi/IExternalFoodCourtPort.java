package com.pragma.powerup.ordermicroservice.domain.spi;

import java.util.List;

public interface IExternalFoodCourtPort {
    boolean dishesBelongToRestaurant(Long restaurantId, List<Long> dishIds);
}
