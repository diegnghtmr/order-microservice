package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter;

import com.pragma.powerup.ordermicroservice.domain.spi.ITraceabilityPort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client.TraceabilityFeignClient;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.TraceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityPort {

    private final TraceabilityFeignClient traceabilityFeignClient;

    @Override
    public void logTrace(String orderId, Long clientId, String employeeEmail, String previousState, String newState) {
        try {
            TraceRequestDto dto = new TraceRequestDto(orderId, clientId, employeeEmail, previousState, newState);
            traceabilityFeignClient.logTrace(dto);
        } catch (Exception e) {
            log.error("Could not save trace for order {}: {}", orderId, e.getMessage());
        }
    }
}
