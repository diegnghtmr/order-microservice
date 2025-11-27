package com.pragma.powerup.ordermicroservice.domain.spi;

public interface IMessagingPort {
    void sendMessage(String phoneNumber, String message);
}
