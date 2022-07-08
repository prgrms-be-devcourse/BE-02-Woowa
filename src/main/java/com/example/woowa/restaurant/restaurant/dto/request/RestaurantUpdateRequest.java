package com.example.woowa.restaurant.restaurant.dto.request;

import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestaurantUpdateRequest {

    private final LocalTime openingTime;

    private final LocalTime closingTime;

    @NotBlank(message = "휴대폰번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
        message = "휴대폰번호 양식이 일치하지 않습니다.")
    private final String phoneNumber;

    private final String address;

    private final String description;

}
