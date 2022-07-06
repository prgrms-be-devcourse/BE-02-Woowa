package com.example.woowa.restaurant.restaurant.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantCreateResponse {

    private final Long id;

    private final Long ownerId;

    private final String name;

    private final String businessNumber;

    private final LocalTime openingTime;

    private final LocalTime closingTime;

    private final Boolean isOpen;

    private final String phoneNumber;

    private final String description;

    private final String address;

    private final List<String> categories;

    private final LocalDateTime createdAt;

}
