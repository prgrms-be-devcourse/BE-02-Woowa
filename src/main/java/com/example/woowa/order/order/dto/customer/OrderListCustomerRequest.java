package com.example.woowa.order.order.dto.customer;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderListCustomerRequest {

    @NotNull
    private final String loginId;

    @NotNull
    @PositiveOrZero
    private final Integer pageNum;

    @NotNull
    @Positive
    private final Integer size;

    @NotNull
    private final LocalDate from;

    @NotNull
    private final LocalDate end;
}
