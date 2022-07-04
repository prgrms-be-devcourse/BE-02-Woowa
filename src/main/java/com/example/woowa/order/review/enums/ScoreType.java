package com.example.woowa.order.review.enums;

import java.util.Arrays;

public enum ScoreType {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    ScoreType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static ScoreType find(int value) {
        return Arrays.stream(values()).filter((e) -> e.value == value).findFirst().orElseThrow(() -> new RuntimeException("1~5 사이 값을 입력하세요"));
    }
}
