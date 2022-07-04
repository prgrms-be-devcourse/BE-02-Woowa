package com.example.woowa.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INVALID_INPUT_VALUE("잘못된 데이터를 입력하였습니다."),
    DUPLICATE_LOGIN_ID("중복된 로그인 ID 입니다."),
    NOTFOUND_LOGIN_ID("없는 로그인 ID 입니다."),
    //AreaCode
    NOT_FOUND_AREA_CODE_ID("없는 지역 정보 입니다."),
    //Delivery
    NOT_FOUND_DELIVERY_ID("없는 배달 정보 입니다."),
    ALREADY_RECEIVE_DELIVERY("이미 처리된 배달 요청 입니다."),
    ALREADY_RECEIVE_DELIVERY("이미 처리된 배달 요청 입니다."),

    INVALID_PERIOD_VALUE("조회 기간의 시작일은 마감일 이전이어야 합니다."),

    NOT_ORDERABLE_AREA("배달 가능 지역이 아닙니다."),
    INVALID_ORDER_STATUS_CODE("잘못된 주문상태 code입니다.");

    private final String message;
}
