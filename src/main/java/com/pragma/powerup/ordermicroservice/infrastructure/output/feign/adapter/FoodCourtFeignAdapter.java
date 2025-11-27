package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter;

import com.pragma.powerup.ordermicroservice.domain.model.DishModel;
import com.pragma.powerup.ordermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client.FoodCourtFeignClient;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.DishResponseDto;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.RestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FoodCourtFeignAdapter implements IExternalFoodCourtPort {

    private final FoodCourtFeignClient foodCourtFeignClient;

    @Override
    public RestaurantModel getRestaurant(Long id) {
        try {
            RestaurantResponseDto response = foodCourtFeignClient.getRestaurant(id);
            return mapToRestaurantModel(response);
        } catch (Exception e) {
            return null; // Or handle specific Feign 404 exception
        }
    }

    @Override
    public DishModel getDish(Long id) {
        try {
            DishResponseDto response = foodCourtFeignClient.getDish(id);
            return mapToDishModel(response);
        } catch (Exception e) {
            return null;
        }
    }

    private RestaurantModel mapToRestaurantModel(RestaurantResponseDto dto) {
        if (dto == null) return null;
        return new RestaurantModel(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getPhone(),
                dto.getUrlLogo(),
                dto.getOwnerId(),
                dto.getNit()
        );
    }

    private DishModel mapToDishModel(DishResponseDto dto) {
        if (dto == null) return null;
        return new DishModel(
                dto.getId(),
                dto.getName(),
                dto.getCategoryId(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getRestaurantId(),
                dto.getUrlImage(),
                dto.getActive()
        );
    }
}