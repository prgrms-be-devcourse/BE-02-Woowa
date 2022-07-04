package com.example.woowa.delivery.dto;

import com.example.woowa.delivery.enums.DeliveryStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeliveryResponse {

    private final Long id;

    private final String restaurantAddress;

    private final String customerAddress;

    private final int deliveryFee;

    private final DeliveryStatus deliveryStatus;

    private final LocalDateTime arrivalTime;
}
