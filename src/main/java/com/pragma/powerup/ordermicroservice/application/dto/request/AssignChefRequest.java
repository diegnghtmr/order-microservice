package com.pragma.powerup.ordermicroservice.application.dto.request;

import jakarta.validation.constraints.NotNull;

public class AssignChefRequest {

    @NotNull
    private Long chefId;

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }
}
