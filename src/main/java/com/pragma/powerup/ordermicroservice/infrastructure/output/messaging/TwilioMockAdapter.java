package com.pragma.powerup.ordermicroservice.infrastructure.output.messaging;

import com.pragma.powerup.ordermicroservice.domain.spi.IMessagingPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TwilioMockAdapter implements IMessagingPort {

    @Override
    public void sendMessage(String phoneNumber, String message) {
        log.info("SMS sending to {}: {}", phoneNumber, message);
    }
}
