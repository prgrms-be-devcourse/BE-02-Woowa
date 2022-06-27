package com.example.woowa.restaurant.advertisement.enums;

import java.util.Arrays;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RateType {

    FLAT("정액제", (price, rate) -> price - rate),
    PERCENT("비율제", (price, rate) -> (int) (price * (1 - rate / 100.0)));

    private final String name;
    private final BiFunction<Integer, Integer, Integer> apply;

    public String getName() {
        return name;
    }

    public static RateType getRateType(String name) {
        return Arrays.stream(values())
            .filter(rateType -> rateType.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요금제입니다."));
    }

}
