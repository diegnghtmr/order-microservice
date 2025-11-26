package com.pragma.powerup.ordermicroservice.domain.spi;

public interface IExternalUserPort {
    boolean userExists(Long userId);
}
