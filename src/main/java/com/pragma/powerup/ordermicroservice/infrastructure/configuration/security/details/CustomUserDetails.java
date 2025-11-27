package com.pragma.powerup.ordermicroservice.infrastructure.configuration.security.details;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails {
    private Long id;
    private Long restaurantId;
    private String username;
}
