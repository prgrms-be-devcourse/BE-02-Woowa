package com.example.woowa.restaurant.advertisement.enums;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RateType {

    FLAT("정액제") {
        @Override
        public int applyRate(int totalPrice, int rate) {
            return totalPrice - rate;
        }
    },
    PERCENT("비율제") {
        @Override
        public int applyRate(int totalPrice, int rate) {
            return (int)Math.round(totalPrice * (1 - rate / 100.0));
        }
    };

    private final String name;

    public String getName() {
        return name;
    }

    public abstract int applyRate(int totalPrice, int rate);

    public static RateType getRateType(String name) {
        return Arrays.stream(values())
            .filter(rateType -> rateType.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 요금제입니다."));
    }

}
