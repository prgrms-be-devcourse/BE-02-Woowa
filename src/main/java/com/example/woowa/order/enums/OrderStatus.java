package com.example.woowa.order.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    PAYMENT_COMPLETED("결제 완료"),
    ACCEPTED("주문 수락"),
    CANCEL("주문 취소");

    private final String description;
}
