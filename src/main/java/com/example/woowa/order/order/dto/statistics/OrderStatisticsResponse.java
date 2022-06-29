package com.example.woowa.order.order.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatisticsResponse {

    private Long orderCount;

    private Long orderPrice;

    private Long discountPrice;

}
