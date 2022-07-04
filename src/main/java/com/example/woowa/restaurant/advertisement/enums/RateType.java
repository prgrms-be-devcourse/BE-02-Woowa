package com.example.woowa.restaurant.advertisement.enums;

import com.example.woowa.common.interfaces.EnumFindable;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RateType implements EnumFindable {

    FLAT("정액제", (price, rate) -> price - rate),
    PERCENT("비율제", (price, rate) -> (int) (price * (1 - rate / 100.0)));

    private final String name;
    private final BiFunction<Integer, Integer, Integer> apply;

    @Override
    public String getType() {
        return this.name;
    }

}
