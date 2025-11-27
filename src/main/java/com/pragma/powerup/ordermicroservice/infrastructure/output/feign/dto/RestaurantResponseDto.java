package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String urlLogo;
    private Long ownerId;
    private String nit;
}
