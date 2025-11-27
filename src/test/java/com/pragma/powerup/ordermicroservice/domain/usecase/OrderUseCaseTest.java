package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.exception.ClientHasActiveOrderException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotActiveException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.model.DishModel;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IExternalFoodCourtPort foodCourtPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private RestaurantModel restaurantModel;
    private DishModel dishModel;

    @BeforeEach
    void init() {
        OrderDish dish = new OrderDish(1L, 2);
        order = new Order(null, 5L, null, null, null, 10L, Collections.singletonList(dish));
        
        restaurantModel = new RestaurantModel(10L, "Resto", "Addr", "123", "logo", 1L, "NIT");
        dishModel = new DishModel(1L, "Dish", 1L, "Desc", 100.0, 10L, "img", true);
    }

    @Test
    void createOrderShouldSetStatusAndDate() {
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(false);
        when(foodCourtPort.getRestaurant(10L)).thenReturn(restaurantModel);
        when(foodCourtPort.getDish(1L)).thenReturn(dishModel);
        when(orderPersistencePort.save(any())).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId("abc123");
            return saved;
        });

        Order created = orderUseCase.createOrder(order);

        assertEquals("abc123", created.getId());
        assertEquals("PENDIENTE", created.getStatus());
        assertNotNull(created.getDate());
        verify(orderPersistencePort).save(any(Order.class));
    }

    @Test
    void createOrderShouldFailWhenClientHasActiveOrder() {
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(true);

        assertThrows(ClientHasActiveOrderException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrderShouldFailWhenRestaurantNotFound() {
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(false);
        when(foodCourtPort.getRestaurant(10L)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrderShouldFailWhenDishNotFound() {
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(false);
        when(foodCourtPort.getRestaurant(10L)).thenReturn(restaurantModel);
        when(foodCourtPort.getDish(1L)).thenReturn(null);

        assertThrows(DishNotFoundException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrderShouldFailWhenDishBelongsToAnotherRestaurant() {
        dishModel.setRestaurantId(99L);
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(false);
        when(foodCourtPort.getRestaurant(10L)).thenReturn(restaurantModel);
        when(foodCourtPort.getDish(1L)).thenReturn(dishModel);

        assertThrows(DishBelongsToAnotherRestaurantException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void createOrderShouldFailWhenDishNotActive() {
        dishModel.setActive(false);
        when(orderPersistencePort.existsActiveOrder(5L)).thenReturn(false);
        when(foodCourtPort.getRestaurant(10L)).thenReturn(restaurantModel);
        when(foodCourtPort.getDish(1L)).thenReturn(dishModel);

        assertThrows(DishNotActiveException.class, () -> orderUseCase.createOrder(order));
    }

    @Test
    void updateStatusShouldFailWhenOrderMissing() {
        when(orderPersistencePort.findById("missing")).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.updateStatus("missing", "READY"));
    }
}