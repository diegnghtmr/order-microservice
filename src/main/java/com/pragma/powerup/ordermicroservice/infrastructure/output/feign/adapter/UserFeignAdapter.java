package com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter;

import com.pragma.powerup.ordermicroservice.domain.model.UserModel;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.client.UserFeignClient;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.dto.UserResponseDto;
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

    @Override
    public UserModel getUserById(Long userId) {
        try {
            UserResponseDto dto = userFeignClient.getUserById(userId);
            if (dto == null) return null;
            return new UserModel(dto.getId(), dto.getName(), dto.getLastName(), dto.getCellPhone());
        } catch (Exception e) {
            return null;
        }
    }
}
