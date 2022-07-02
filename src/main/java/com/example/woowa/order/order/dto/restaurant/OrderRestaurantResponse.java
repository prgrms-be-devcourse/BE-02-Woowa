package com.example.woowa.order.order.dto.restaurant;

import com.example.woowa.order.order.dto.cart.CartResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderRestaurantResponse {

    private final LocalDateTime createdAt;
    private final List<CartResponse> menus;
    private final Integer orderPrice;
    private final Integer deliveryFee;
    private final Integer afterDiscountTotalPrice;
    private final Integer totalDiscountPrice;
    private final String orderStatus;
}
