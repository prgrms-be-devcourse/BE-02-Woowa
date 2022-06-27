package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerGradeFindResponse {
    private Long id;
    private Integer orderCount;
    private String grade;
    private Integer discountPrice;
    private Integer voucherCount;
}
