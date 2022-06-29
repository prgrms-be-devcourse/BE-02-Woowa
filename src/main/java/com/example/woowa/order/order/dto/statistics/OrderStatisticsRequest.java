package com.example.woowa.order.order.dto.statistics;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatisticsRequest {

    @NotNull
    Long restaurantId;

    @NotNull
    LocalDate from;

    @NotNull
    LocalDate end;

}
