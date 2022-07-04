package com.example.woowa.restaurant.restaurant.dto.request;

import java.time.LocalTime;
import java.util.List;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@RequiredArgsConstructor
public class RestaurantCreateRequest {

    @Pattern(regexp = "[가-힣]{1,10}", message = "이름은 최대 10자이며 공백을 포함할 수 없습니다.")
    private final String name;

    // validate
    private final String businessNumber;

    // validate
    @DateTimeFormat(pattern = "HH:mm:ss")
    private final LocalTime openingTime;

    // validate
    @DateTimeFormat(pattern = "HH:mm:ss")
    private final LocalTime closingTime;

    private final Boolean isOpen;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 휴대 전화번호 형식이어야 합니다.")
    private final String phoneNumber;

    private final String description;

    // validate
    private final String address;

    private final List<Long> categoryIds;

}
