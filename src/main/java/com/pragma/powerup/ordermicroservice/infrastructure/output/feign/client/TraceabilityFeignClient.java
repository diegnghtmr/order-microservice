package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client;

import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.TraceRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability-microservice", url = "http://localhost:8093/traceability-api")
public interface TraceabilityFeignClient {
    @PostMapping("/trace")
    void logTrace(@RequestBody TraceRequestDto traceRequestDto);
}
