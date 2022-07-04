package com.example.woowa.order.order.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class OrderAcceptRequest {

    @NotNull
    @Positive
    private final Integer cookingTime;

    public OrderAcceptRequest(@JsonProperty("cookingTime") Integer cookingTime) {
        this.cookingTime = cookingTime;
    }
}
