package com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    private String id;
    private Long clientId;
    private Date date;
    private String status;
    private Long chefId;
    private Long restaurantId;
    private List<OrderDishEntity> dishes;
    private String pin;
}
