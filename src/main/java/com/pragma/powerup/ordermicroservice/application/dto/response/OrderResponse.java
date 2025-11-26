package com.pragma.powerup.ordermicroservice.application.dto.response;

import java.util.Date;
import java.util.List;

public class OrderResponse {
    private String id;
    private Long clientId;
    private Date date;
    private String status;
    private Long chefId;
    private Long restaurantId;
    private List<OrderDishResponse> dishes;

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

    public List<OrderDishResponse> getDishes() {
        return dishes;
    }

    public void setDishes(List<OrderDishResponse> dishes) {
        this.dishes = dishes;
    }
}
