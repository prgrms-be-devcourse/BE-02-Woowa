package com.example.woowa.delivery.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DeliveryStatus {

    DELIVERY_WAITING("배차 대기 중"), DELIVERY_REGISTRATION("배차 등록"),
    DELIVERY_PICKUP("픽업 완료"), DELIVERY_FINISH("배달 완료");

    private final String description;
}
