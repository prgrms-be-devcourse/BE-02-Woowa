package com.example.woowa.restaurant.advertisement.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdvertisementCreateRequest {

    @Size(min = 1, max = 30, message = "광고 이름은 1자이상 30자 이하여야 합니다.")
    private final String title;

    // validate
    private final String unitType;

    // validate
    private final String rateType;

    // validate
    @Min(value = 0, message = "rate값은 음수일 수 없습니다.")
    private final Integer rate;

    @Size(min = 1, max = 1000, message = "광고 설명은 1자이상 1000자 이하여야 합니다.")
    private final String description;

    @Min(value = 10, message = "한 광고에 포함될 수 있는 가게는 10개이상 이어야합니다.")
    @Max(value = 50, message = "한 광고에 포함될 수 있는 가게는 50개이하 이어야합니다.")
    private final Integer limitSize;

}
