package com.example.woowa.restaurant.restaurant.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantFindResponse {

    private final Long id;

    private final Long ownerId;

    private final String name;

    private final String businessNumber;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalTime openingTime;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalTime closingTime;

    private final Boolean isOpen;

    private final String phoneNumber;

    private final String description;

    private final Double averageReviewScore;

    private final Integer reviewCount;

    private final String address;

    private final List<String> categories;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

}
