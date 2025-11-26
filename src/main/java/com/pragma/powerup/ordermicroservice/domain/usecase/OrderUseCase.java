package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidDishesException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalUserPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderUseCase implements IOrderServicePort {

    private static final String DEFAULT_STATUS = "PENDING";
    private final IOrderPersistencePort orderPersistencePort;
    private final IExternalUserPort userPort;
    private final IExternalFoodCourtPort foodCourtPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IExternalUserPort userPort,
                        IExternalFoodCourtPort foodCourtPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userPort = userPort;
        this.foodCourtPort = foodCourtPort;
    }

    @Override
    public Order createOrder(Order order) {
        validateClient(order.getClientId());
        validateDishes(order.getRestaurantId(), order.getDishes());
        order.setDate(new Date());
        order.setStatus(DEFAULT_STATUS);
        return orderPersistencePort.save(order);
    }

    @Override
    public Order assignChef(String orderId, Long chefId) {
        Order order = findOrThrow(orderId);
        order.setChefId(chefId);
        return orderPersistencePort.update(order);
    }

    @Override
    public Order updateStatus(String orderId, String status) {
        Order order = findOrThrow(orderId);
        order.setStatus(status);
        return orderPersistencePort.update(order);
    }

    @Override
    public Order getById(String orderId) {
        return findOrThrow(orderId);
    }

    @Override
    public List<Order> getByRestaurantAndStatus(Long restaurantId, String status) {
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status);
    }

    private Order findOrThrow(String orderId) {
        Optional<Order> optionalOrder = orderPersistencePort.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        return optionalOrder.get();
    }

    private void validateClient(Long clientId) {
        if (clientId == null || !userPort.userExists(clientId)) {
            throw new InvalidClientException(clientId);
        }
    }

    private void validateDishes(Long restaurantId, List<OrderDish> dishes) {
        if (restaurantId == null || dishes == null || dishes.isEmpty()) {
            throw new InvalidDishesException(restaurantId);
        }
        List<Long> dishIds = dishes.stream()
                .map(OrderDish::getDishId)
                .collect(Collectors.toList());
        if (!foodCourtPort.dishesBelongToRestaurant(restaurantId, dishIds)) {
            throw new InvalidDishesException(restaurantId);
        }
    }
}
