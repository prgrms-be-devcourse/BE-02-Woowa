package com.example.woowa.restaurant.advertisement.enums;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UnitType {

    PER_ORDER("주문별"),
    MOTHLY("월별");

    private final String name;

    public String getName() {
        return this.name;
    }

    public static UnitType getUnitType(String name) {
        return Arrays.stream(values())
            .filter(unitType -> unitType.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 청구방식입니다."));
    }

}
