package com.example.woowa.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INVALID_INPUT_VALUE("잘못된 데이터를 입력하였습니다."),
    INVALID_PERIOD_VALUE("조회 기간의 시작일은 마감일 이전이어야 합니다."),

    NOT_ORDERABLE_AREA("배달 가능 지역이 아닙니다.");

    private final String message;
}
