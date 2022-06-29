package com.example.woowa.order.order.converter;

import com.example.woowa.order.order.dto.cart.CartResponse;
import com.example.woowa.order.order.entity.Cart;

public abstract class CartConverter {

    public static CartResponse toCartResponse(Cart cart) {
        return new CartResponse(cart.getMenu().getTitle(), cart.getQuantity(),
                cart.getMenu().getPrice() * cart.getQuantity());
    }
}
