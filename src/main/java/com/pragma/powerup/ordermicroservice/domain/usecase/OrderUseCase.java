package com.pragma.powerup.ordermicroservice.domain.usecase;

import com.pragma.powerup.ordermicroservice.domain.api.IOrderServicePort;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class OrderUseCase implements IOrderServicePort {

    private static final String DEFAULT_STATUS = "PENDIENTE";
    private final IOrderPersistencePort orderPersistencePort;
    private final IExternalFoodCourtPort foodCourtPort;
    private final IExternalUserPort userPort;
    private final IMessagingPort messagingPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IExternalFoodCourtPort foodCourtPort,
                        IExternalUserPort userPort,
                        IMessagingPort messagingPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.foodCourtPort = foodCourtPort;
        this.userPort = userPort;
        this.messagingPort = messagingPort;
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
    public void cancelOrder(String orderId, Long userId) {
        Order order = findOrThrow(orderId);

        if (!order.getClientId().equals(userId)) {
            throw new OrderNotBelongToClientException("Order does not belong to client");
        }

        if (!"PENDIENTE".equals(order.getStatus())) {
            throw new OrderNotCancellableException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        order.setStatus("CANCELADO");
        orderPersistencePort.update(order);
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

    @Override
    public OrderPage getAllOrdersByStatus(Integer page, Integer size, String status, Long restaurantId) {
        if (restaurantId == null) {
            throw new EmployeeNotBelongToRestaurantException();
        }
        return orderPersistencePort.findByRestaurantIdAndStatus(restaurantId, status, page, size);
    }

    @Override
    public Order assignOrder(String orderId, Long employeeId, Long restaurantId) {
        Order order = findOrThrow(orderId);

        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new OrderBelongsToAnotherRestaurantException("Order belongs to another restaurant");
        }

        if (!"PENDIENTE".equals(order.getStatus())) {
            throw new OrderNotPendingException("Order is not pending and cannot be assigned");
        }
        
        if (order.getChefId() != null) {
             throw new OrderAlreadyAssignedException("Order is already assigned");
        }

        order.setChefId(employeeId);
        order.setStatus("EN_PREPARACION");
        return orderPersistencePort.update(order);
    }

    @Override
    public Order markOrderReady(String orderId, Long employeeId, Long restaurantId) {
        Order order = findOrThrow(orderId);

        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new OrderBelongsToAnotherRestaurantException("Order belongs to another restaurant");
        }

        if (!"EN_PREPARACION".equals(order.getStatus())) {
            throw new OrderNotPreparedException("Order is not in preparation");
        }

        String pin = generatePin();
        order.setStatus("LISTO");
        order.setPin(pin);
        Order updatedOrder = orderPersistencePort.update(order);

        notifyClient(order.getClientId(), pin);

        return updatedOrder;
    }

    @Override
    public void deliverOrder(String orderId, String pin) {
        Order order = findOrThrow(orderId);

        if (!"LISTO".equals(order.getStatus())) {
            throw new OrderNotReadyException("Order is not ready for delivery");
        }
        
        if (order.getPin() == null || !order.getPin().equals(pin)) {
            throw new SecurityPinMismatchException();
        }

        order.setStatus("ENTREGADO");
        orderPersistencePort.update(order);
    }

    private void notifyClient(Long clientId, String pin) {
        UserModel user = userPort.getUserById(clientId);
        if (user != null && user.getCellPhone() != null) {
            messagingPort.sendMessage(user.getCellPhone(), "Tu pedido está listo. Usa el PIN " + pin + " para retirarlo.");
        }
    }

    private String generatePin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

    private Order findOrThrow(String orderId) {
        Optional<Order> optionalOrder = orderPersistencePort.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        return optionalOrder.get();
    }
}