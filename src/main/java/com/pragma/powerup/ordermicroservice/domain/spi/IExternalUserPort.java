package com.pragma.powerup.ordermicroservice.domain.spi;

import com.pragma.powerup.ordermicroservice.domain.model.UserModel;

public interface IExternalUserPort {
    boolean userExists(Long userId);
    UserModel getUserById(Long userId);
}