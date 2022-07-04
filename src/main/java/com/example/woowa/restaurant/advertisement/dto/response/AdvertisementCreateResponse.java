package com.example.woowa.restaurant.advertisement.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdvertisementCreateResponse {

    private final Long id;

    private final String title;

    private final String unitType;

    private final String rateType;

    private final Integer rate;

    private final String description;

    private final Integer limitSize;

    private final Integer currentSize;

    private final LocalDateTime createdAt;

}
