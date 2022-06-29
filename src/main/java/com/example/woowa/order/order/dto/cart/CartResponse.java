package com.example.woowa.order.order.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartResponse {

    private String menuName;
    private Integer quantity;
    private Integer totalPrice;
}
