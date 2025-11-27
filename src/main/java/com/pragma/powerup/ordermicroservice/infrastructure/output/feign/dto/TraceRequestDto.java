package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceRequestDto {
    private String orderId;
    private Long clientId;
    private String employeeEmail;
    private String previousState;
    private String newState;
}
