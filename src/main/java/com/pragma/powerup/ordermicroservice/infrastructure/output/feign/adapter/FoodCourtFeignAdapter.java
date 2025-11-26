package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter;

import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client.FoodCourtFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FoodCourtFeignAdapter implements IExternalFoodCourtPort {

    private final FoodCourtFeignClient foodCourtFeignClient;

    @Override
    public boolean dishesBelongToRestaurant(Long restaurantId, List<Long> dishIds) {
        Boolean valid = foodCourtFeignClient.validateDishes(restaurantId, dishIds);
        return valid != null && valid;
    }
}
