package com.example.woowa.order.order.dto.restaurant;

import com.example.woowa.order.order.dto.cart.CartResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderRestaurantResponse {

    private LocalDateTime createdAt;

    List<CartResponse> menus;

    Integer orderPrice;

    Integer deliveryFee;

    Integer afterDiscountTotalPrice;

    Integer totalDiscountPrice;

    String orderStatus;
}
