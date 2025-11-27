package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.api.IOrderServicePort;
import com.pragma.powerup.ordermicroservice.domain.exception.ClientHasActiveOrderException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishBelongsToAnotherRestaurantException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotActiveException;
import com.pragma.powerup.ordermicroservice.domain.exception.DishNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidClientException;
import com.pragma.powerup.ordermicroservice.domain.exception.InvalidDishesException;
import com.pragma.powerup.ordermicroservice.domain.exception.OrderNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.ordermicroservice.domain.model.DishModel;
import com.pragma.powerup.ordermicroservice.domain.model.Order;
import com.pragma.powerup.ordermicroservice.domain.model.OrderDish;
import com.pragma.powerup.ordermicroservice.domain.model.RestaurantModel;
import com.pragma.powerup.ordermicroservice.domain.spi.IExternalFoodCourtPort;
import com.pragma.powerup.ordermicroservice.domain.spi.IOrderPersistencePort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderUseCase implements IOrderServicePort {

    private static final String DEFAULT_STATUS = "PENDIENTE";
    private final IOrderPersistencePort orderPersistencePort;
    private final IExternalFoodCourtPort foodCourtPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IExternalFoodCourtPort foodCourtPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.foodCourtPort = foodCourtPort;
    }

    @Override
    public Order createOrder(Order order) {
        if (orderPersistencePort.existsActiveOrder(order.getClientId())) {
            throw new ClientHasActiveOrderException(order.getClientId());
        }

        RestaurantModel restaurant = foodCourtPort.getRestaurant(order.getRestaurantId());
        if (restaurant == null) {
            throw new RestaurantNotFoundException(order.getRestaurantId());
        }

        for (OrderDish orderDish : order.getDishes()) {
            DishModel dish = foodCourtPort.getDish(orderDish.getDishId());
            if (dish == null) {
                throw new DishNotFoundException(orderDish.getDishId());
            }
            if (!dish.getRestaurantId().equals(order.getRestaurantId())) {
                throw new DishBelongsToAnotherRestaurantException(orderDish.getDishId(), order.getRestaurantId());
            }
            if (!Boolean.TRUE.equals(dish.getActive())) {
                throw new DishNotActiveException(orderDish.getDishId());
            }
        }

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
}
