package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client;

import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-microservice", url = "${clients.user.url}")
public interface UserFeignClient {

    @GetMapping("/users/{id}/exists")
    Boolean userExists(@PathVariable("id") Long userId);

    @GetMapping("/users/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
}
