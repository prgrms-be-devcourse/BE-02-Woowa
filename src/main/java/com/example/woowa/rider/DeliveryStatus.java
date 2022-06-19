package com.example.woowa.rider;

import lombok.RequiredArgsConstructor;

/**
 * 접수 완료 - 픽업 완료 - 배달 완료
 * ✔︎ 접수완료 : 사장님이 주문을 접수하고 배달예상시간을 안내한 단계 - (배차 대기중, 배차 완료)
 * ✔︎ 픽업완료 : 라이더가 가게에 방문해서 배달 음식 픽업을 완료 후 배달을 시작한 단계
 * ✔︎ 배달완료 : 라이더가 고객에게 주문한 음식을 배달 완료한 단계
 */
@RequiredArgsConstructor
public enum DeliveryStatus {
    DELIVERY_WAITING("배차 대기 중"), DELIVERY_REGISTRATION("배차 등록"), DELIVERY_PICKUP("픽업 완료"), DELIVERY_FINUSH("배달 완료");

    private final String description;
}
