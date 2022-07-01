package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCustomerResponse {

    private LocalDateTime createdAt;

    private List<CartResponse> menus;

    private Integer beforeDiscountTotalPrice;

    private Integer voucherDiscountPrice;

    private Integer usedPoint;

    private String orderStatus;

    private String deliveryAddress;
}
