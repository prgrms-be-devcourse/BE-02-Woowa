package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerFindResponse {
    private final String loginId;
    private final String birthdate;
    private final Integer orderPerMonth;
    private final Integer point;
    private final Boolean isIssued;
    private final CustomerGradeFindResponse customerGrade;
}
