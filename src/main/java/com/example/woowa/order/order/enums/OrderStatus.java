package com.example.woowa.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PAYMENT_COMPLETED("결제 완료"),
    ACCEPTED("주문 수락"),
    CANCEL("주문 취소");

    private final String description;
}
