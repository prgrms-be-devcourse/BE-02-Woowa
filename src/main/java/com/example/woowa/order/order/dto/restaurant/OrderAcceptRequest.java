package com.example.woowa.order.order.dto.restaurant;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class OrderAcceptRequest {

    @NotNull(message = "조리 예상 시간은 필수입니다.")
    @Positive(message = "조리 예상 시간은 0보다 커야 합니다.")
    private final Integer cookingTime;

    public OrderAcceptRequest(@JsonProperty("cookingTime") Integer cookingTime) {
        this.cookingTime = cookingTime;
    }
}
