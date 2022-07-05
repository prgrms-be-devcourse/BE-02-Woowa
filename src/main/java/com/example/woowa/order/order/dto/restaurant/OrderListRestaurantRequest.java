package com.example.woowa.order.order.dto.restaurant;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class OrderListRestaurantRequest {

    @NotNull(message = "가게 ID는 필수입니다.")
    private final Long restaurantId;

    @NotNull(message = "페이지 번호는 필수입니다.")
    @PositiveOrZero(message = "페이지 번호는 0부터 시작입니다.")
    private final Integer pageNum;

    @NotNull(message = "목록 개수는 필수입니다.")
    @Positive(message = "목록 개수는 0보다 커야합니다.")
    private final Integer size;

    @NotNull(message = "조회 시작일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate from;

    @NotNull(message = "조회 종료일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate end;
}
