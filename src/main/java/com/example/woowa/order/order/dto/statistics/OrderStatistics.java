package com.example.woowa.order.order.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderStatistics {

    private final Long orderCount;
    private final Long orderPrice;
    private final Long voucherDiscountPrice;
    private final Long usedPoint;
}
