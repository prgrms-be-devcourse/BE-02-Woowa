package com.example.woowa.delivery.dto;

import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeliveryCreateRequest {

    private final String restaurantAddress;

    private final String customerAddress;

    @Positive(message = "배달료는 양수 입니다.")
    private final int deliveryFee;

    @Positive(message = "orderId는 양수 입니다.")
    private Long orderId;
}
