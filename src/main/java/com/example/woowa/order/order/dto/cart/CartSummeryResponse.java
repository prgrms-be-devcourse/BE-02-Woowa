package com.example.woowa.order.order.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartSummeryResponse {

    private String menuTitle;

    private Integer quantity;
}
