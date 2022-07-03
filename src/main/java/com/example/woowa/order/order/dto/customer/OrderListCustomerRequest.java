package com.example.woowa.order.order.dto.customer;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate from;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate end;
}
