package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonAlias("imageUrl")
    private String urlImage;
    private Boolean active;
}
