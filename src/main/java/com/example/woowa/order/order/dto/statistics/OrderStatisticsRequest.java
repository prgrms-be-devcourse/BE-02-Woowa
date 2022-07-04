package com.example.woowa.order.order.dto.statistics;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class OrderStatisticsRequest {

    @NotNull
    private final Long restaurantId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate from;

    @NotNull
    private final LocalDate end;

}
