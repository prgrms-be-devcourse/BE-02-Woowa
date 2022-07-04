package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class CustomerGradeFindResponse {
    private final Long id;
    private final Integer orderCount;
    @Length(max = 10)
    private final String title;
    private final Integer discountPrice;
    private final Integer voucherCount;
}

