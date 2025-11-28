package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.exception.ClientHasActiveOrderException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotActiveException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.EmployeeNotBelongToRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderAlreadyAssignedException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotBelongToClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotCancellableException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotPendingException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotPreparedException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotReadyException;
import com.pragma.powerup.ordermicroservice.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.SecurityPinMismatchException;
import com.pragma.powerup.ordermicroservice.domain.model.DishModel;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.domain.model.OrderPage;
import com.pragma.powerup.ordermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.ordermicroservice.domain.model.UserModel;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IMessagingPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.ordermicroservice.domain.spi.ITraceabilityPort;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IExternalFoodCourtPort foodCourtPort;

    @Mock
    private IExternalUserPort userPort;

    @Mock
    private IMessagingPort messagingPort;

    @Mock
    private ITraceabilityPort traceabilityPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private RestaurantModel restaurantModel;
    private DishModel dishModel;

    @BeforeEach
    void init() {
        OrderDish dish = new OrderDish(1L, 2);
        order = new Order(null, 5L, null, null, null, 10L, Collections.singletonList(dish), null);
        
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

    @Test
    void getAllOrdersByStatusShouldReturnPage() {
        OrderPage page = new OrderPage(Collections.singletonList(order), 1, 1);
        when(orderPersistencePort.findByRestaurantIdAndStatus(10L, "PENDIENTE", 0, 10)).thenReturn(page);

        OrderPage result = orderUseCase.getAllOrdersByStatus(0, 10, "PENDIENTE", 10L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllOrdersByStatusShouldFailWhenRestaurantIdIsNull() {
        assertThrows(EmployeeNotBelongToRestaurantException.class, 
            () -> orderUseCase.getAllOrdersByStatus(0, 10, "PENDIENTE", null));
    }
    
    @Test
    void assignOrderShouldSucceedWhenConditionsMet() {
        order.setId("123");
        order.setStatus("PENDIENTE");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));
        when(orderPersistencePort.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updated = orderUseCase.assignOrder("123", 99L, 10L, "chef@test.com");

        assertEquals("EN_PREPARACION", updated.getStatus());
        assertEquals(99L, updated.getChefId());
    }

    @Test
    void assignOrderShouldFailWhenOrderDifferentRestaurant() {
        order.setId("123");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderBelongsToAnotherRestaurantException.class, 
            () -> orderUseCase.assignOrder("123", 99L, 11L, "chef@test.com"));
    }

    @Test
    void assignOrderShouldFailWhenOrderNotPending() {
        order.setId("123");
        order.setStatus("READY");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderNotPendingException.class, 
            () -> orderUseCase.assignOrder("123", 99L, 10L, "chef@test.com"));
    }

    @Test
    void assignOrderShouldFailWhenAlreadyAssigned() {
        order.setId("123");
        order.setStatus("PENDIENTE");
        order.setChefId(55L);
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyAssignedException.class, 
            () -> orderUseCase.assignOrder("123", 99L, 10L, "chef@test.com"));
    }

    @Test
    void markOrderReadyShouldSucceedAndNotify() {
        order.setId("123");
        order.setStatus("EN_PREPARACION");
        order.setClientId(5L);
        UserModel user = new UserModel(5L, "Pepe", "Perez", "+1234567890");
        
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));
        when(userPort.getUserById(5L)).thenReturn(user);
        when(orderPersistencePort.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order readyOrder = orderUseCase.markOrderReady("123", 99L, 10L, "chef@test.com");

        assertEquals("LISTO", readyOrder.getStatus());
        assertNotNull(readyOrder.getPin());
        verify(messagingPort).sendMessage(eq("+1234567890"), any(String.class));
    }

    @Test
    void markOrderReadyShouldFailWhenOrderNotPrepared() {
        order.setId("123");
        order.setStatus("PENDIENTE");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderNotPreparedException.class, 
            () -> orderUseCase.markOrderReady("123", 99L, 10L, "chef@test.com"));
    }

    @Test
    void deliverOrderShouldSucceedWithCorrectPin() {
        order.setId("123");
        order.setStatus("LISTO");
        order.setPin("123456");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));
        when(orderPersistencePort.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderUseCase.deliverOrder("123", "123456", "employee@test.com");

        assertEquals("ENTREGADO", order.getStatus());
    }

    @Test
    void deliverOrderShouldFailWithIncorrectPin() {
        order.setId("123");
        order.setStatus("LISTO");
        order.setPin("123456");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(SecurityPinMismatchException.class, () -> orderUseCase.deliverOrder("123", "000000", "employee@test.com"));
    }

    @Test
    void deliverOrderShouldFailWhenStatusNotReady() {
        order.setId("123");
        order.setStatus("PENDIENTE");
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderNotReadyException.class, () -> orderUseCase.deliverOrder("123", "123456", "employee@test.com"));
    }

    @Test
    void cancelOrderShouldSucceedWhenPending() {
        order.setId("123");
        order.setStatus("PENDIENTE");
        order.setClientId(5L);
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));
        when(orderPersistencePort.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderUseCase.cancelOrder("123", 5L);

        assertEquals("CANCELADO", order.getStatus());
    }

    @Test
    void cancelOrderShouldFailWhenNotOwner() {
        order.setId("123");
        order.setClientId(5L);
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderNotBelongToClientException.class, () -> orderUseCase.cancelOrder("123", 99L));
    }

    @Test
    void cancelOrderShouldFailWhenNotPending() {
        order.setId("123");
        order.setStatus("EN_PREPARACION");
        order.setClientId(5L);
        when(orderPersistencePort.findById("123")).thenReturn(Optional.of(order));

        assertThrows(OrderNotCancellableException.class, () -> orderUseCase.cancelOrder("123", 5L));
    }
}
