package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderSummeryResponse {

    private final Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final String orderStatus;
    private final String restaurantName;
    private final Integer afterDiscountTotalPrice;
    private final List<CartSummeryResponse> carts;
}
