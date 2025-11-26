package com.pragma.powerup.ordermicroservice.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private String id;
    private Long clientId;
    private Date date;
    private String status;
    private Long chefId;
    private Long restaurantId;
    private List<OrderDish> dishes;

    public Order() {
        this.dishes = new ArrayList<>();
    }

    public Order(String id, Long clientId, Date date, String status, Long chefId, Long restaurantId, List<OrderDish> dishes) {
        this.id = id;
        this.clientId = clientId;
        this.date = date;
        this.status = status;
        this.chefId = chefId;
        this.restaurantId = restaurantId;
        this.dishes = dishes == null ? new ArrayList<>() : dishes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<OrderDish> getDishes() {
        if (dishes == null) {
            dishes = new ArrayList<>();
        }
        return dishes;
    }

    public void setDishes(List<OrderDish> dishes) {
        this.dishes = dishes == null ? new ArrayList<>() : dishes;
    }

    public boolean hasDishes() {
        return dishes != null && !dishes.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
