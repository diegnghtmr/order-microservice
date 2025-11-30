package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    @JsonAlias("firstName")
    private String name;
    private String lastName;
    @JsonAlias({"phone", "cellPhone"})
    private String cellPhone;
    private Long idRestaurant;
}
