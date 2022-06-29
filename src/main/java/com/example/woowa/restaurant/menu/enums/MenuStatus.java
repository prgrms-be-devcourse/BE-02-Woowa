package com.example.woowa.restaurant.menu.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum MenuStatus {
    SALE("sale", "판매중"), SOLD_OUT("sold_out", "품절"), HIDDEN("hidden", "숨김");

    private final String code;
    private final String viewName;

    MenuStatus(String code, String viewName) {
        this.code = code;
        this.viewName = viewName;
    }

    @JsonValue
    public String getViewName() {
        return viewName;
    }

    @JsonCreator
    public static MenuStatus find(@JsonProperty("menuStatus") String code) {
        return Arrays.stream(MenuStatus.values())
                .filter(status -> status.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("code와 일치하는 타입이 없습니다."));
    }
}
