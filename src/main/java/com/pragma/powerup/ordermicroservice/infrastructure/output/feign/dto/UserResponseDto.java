package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String cellPhone;
}
