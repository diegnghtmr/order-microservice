package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "food-court-microservice", url = "${clients.foodcourt.url}")
public interface FoodCourtFeignClient {

    @PostMapping("/dishes/validate")
    Boolean validateDishes(@RequestParam("restaurantId") Long restaurantId, @RequestBody List<Long> dishIds);
}
