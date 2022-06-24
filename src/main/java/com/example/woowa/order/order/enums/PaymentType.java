package com.example.woowa.order.order.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {
    CREDIT_CARD("신용 카드"), MOBILE_PHONE("휴대폰 결제"), NAVER_PAY("네이버 페이"), KAKAO_PAY(
            "카카오 페이"), TOSS_PAY("토스 페이");

    private final String description;
}
