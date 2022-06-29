package com.example.woowa.order.order.dto.restaurant;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListRestaurantRequest {

    @NotNull
    private Long restaurantId;

    @NotNull
    @PositiveOrZero
    private Integer pageNum;

    @NotNull
    @Positive
    private Integer size;

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate end;
}
