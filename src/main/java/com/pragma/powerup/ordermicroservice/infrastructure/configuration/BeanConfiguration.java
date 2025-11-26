package com.pragma.powerup.ordermicroservice.infrastructure.configuration;

import com.pragma.powerup.ordermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.ordermicroservice.domain.usecase.OrderUseCase;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter.FoodCourtFeignAdapter;
import com.pragma.powerup.ordermicroservice.infrastructure.output.feign.adapter.UserFeignAdapter;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.adapter.OrderMongoAdapter;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.mapper.IOrderEntityMapper;
import com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final UserFeignAdapter userFeignAdapter;
    private final FoodCourtFeignAdapter foodCourtFeignAdapter;

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderMongoAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public IExternalUserPort externalUserPort() {
        return userFeignAdapter;
    }

    @Bean
    public IExternalFoodCourtPort externalFoodCourtPort() {
        return foodCourtFeignAdapter;
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), externalUserPort(), externalFoodCourtPort());
    }
}
