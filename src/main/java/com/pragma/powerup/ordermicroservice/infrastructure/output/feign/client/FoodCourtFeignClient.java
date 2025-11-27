package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client;

import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.DishResponseDto;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.RestaurantResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "food-court-microservice", url = "${clients.foodcourt.url}")
public interface FoodCourtFeignClient {

    @GetMapping("/restaurant/{id}")
    RestaurantResponseDto getRestaurant(@PathVariable("id") Long id);

    @GetMapping("/dish/{id}")
    DishResponseDto getDish(@PathVariable("id") Long id);
}
