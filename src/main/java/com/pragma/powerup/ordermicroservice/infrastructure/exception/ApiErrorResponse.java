package com.pragma.powerup.ordermicroservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private int status;
    private Instant timestamp;
}
