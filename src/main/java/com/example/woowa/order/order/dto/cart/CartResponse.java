package com.example.woowa.order.order.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartResponse {

    private final String menuName;
    private final Integer quantity;
    private final Integer totalPrice;
}
