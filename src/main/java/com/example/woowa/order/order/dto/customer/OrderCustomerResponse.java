package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCustomerResponse {

    private final LocalDateTime createdAt;
    private final List<CartResponse> menus;
    private final Integer orderPrice;
    private final Integer deliveryFee;
    private final Integer voucherDiscountPrice;
    private final Integer usedPoint;
    private final String orderStatus;
    private final String deliveryAddress;
}
