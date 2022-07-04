package com.example.woowa.order.order.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartSummeryResponse {

    private final String menuTitle;
    private final Integer quantity;
}
