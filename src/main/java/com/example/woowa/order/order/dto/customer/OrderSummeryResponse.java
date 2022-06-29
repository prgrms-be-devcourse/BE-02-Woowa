package com.example.woowa.order.order.dto.customer;

import com.example.woowa.order.order.dto.cart.CartSummeryResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderSummeryResponse {

    private Long orderId;

    private LocalDateTime createdAt;

    private String orderStatus;

    private String restaurantName;

    private Integer afterDiscountPrice;

    private List<CartSummeryResponse> carts;
}
