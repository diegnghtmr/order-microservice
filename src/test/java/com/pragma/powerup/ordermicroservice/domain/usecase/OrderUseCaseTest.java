package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.exception.InvalidClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidDishesException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IExternalUserPort userPort;

    @Mock
    private IExternalFoodCourtPort foodCourtPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;

    @BeforeEach
    void init() {
        OrderDish dish = new OrderDish(1L, 2);
        order = new Order(null, 5L, null, null, null, 10L, Collections.singletonList(dish));
    }

    @Test
    void createOrderShouldSetStatusAndDate() {
        when(userPort.userExists(5L)).thenReturn(true);
        when(foodCourtPort.dishesBelongToRestaurant(eq(10L), any())).thenReturn(true);
        when(orderPersistencePort.save(any())).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId("abc123");
            return saved;
        });

        Order created = orderUseCase.createOrder(order);

        assertEquals("abc123", created.getId());
        assertEquals("PENDING", created.getStatus());
        assertNotNull(created.getDate());
    }

    @Test
    void createOrderShouldFailWhenClientNotFound() {
        when(userPort.userExists(5L)).thenReturn(false);

        assertThrows(InvalidClientException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrderShouldFailWhenDishesInvalid() {
        when(userPort.userExists(5L)).thenReturn(true);
        when(foodCourtPort.dishesBelongToRestaurant(eq(10L), any())).thenReturn(false);

        assertThrows(InvalidDishesException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void updateStatusShouldFailWhenOrderMissing() {
        when(orderPersistencePort.findById("missing")).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.updateStatus("missing", "READY"));
    }
}
