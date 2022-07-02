package com.example.woowa.order.order.dto.statistics;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatisticsRequest {

    @NotNull
    private final Long restaurantId;

    @NotNull
    private final LocalDate from;

    @NotNull
    private final LocalDate end;

}
