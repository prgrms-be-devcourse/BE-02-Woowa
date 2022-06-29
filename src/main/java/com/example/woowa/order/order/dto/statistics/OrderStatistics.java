package com.example.woowa.order.order.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatistics {

    private Long orderCount;

    private Long orderPrice;

    private Long voucherDiscountPrice;

    private Long usedPoint;
}
