package com.example.woowa.restaurant.advertisement.enums;


import com.example.woowa.common.interfaces.EnumFindable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UnitType implements EnumFindable {

    PER_ORDER("주문별"),
    MOTHLY("월별");

    private final String name;

    @Override
    public String getType() {
        return this.name;
    }

}
