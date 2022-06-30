package com.example.woowa.restaurant.menu.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuStatus {
    SALE("sale", "판매중"), SOLD_OUT("sold_out", "품절"), HIDDEN("hidden", "숨김");

    private final String code;
    private final String viewName;

    public static MenuStatus find(String requestCode) {
        return Arrays.stream(MenuStatus.values())
                .filter(status -> status.getCode().equals(requestCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("requestCode와 일치하는 타입이 없습니다."));
    }
}
