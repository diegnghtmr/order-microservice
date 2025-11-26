package com.pragma.powerup.ordermicroservice.infrastructure.output.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDishEntity {
    @Field("dish_id")
    private Long dishId;
    private Integer quantity;
}
