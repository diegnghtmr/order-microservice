package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter;

import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignAdapter implements IExternalUserPort {

    private final UserFeignClient userFeignClient;

    @Override
    public boolean userExists(Long userId) {
        Boolean exists = userFeignClient.userExists(userId);
        return exists != null && exists;
    }
}
