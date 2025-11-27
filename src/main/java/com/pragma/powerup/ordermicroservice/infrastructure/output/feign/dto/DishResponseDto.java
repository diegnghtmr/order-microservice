package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDto {
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Double price;
    private Long restaurantId;
    private String urlImage;
    private Boolean active;
}
